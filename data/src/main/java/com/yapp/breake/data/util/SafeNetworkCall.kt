package com.yapp.breake.data.util

import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.message
import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.data.model.NetworkException.ForbiddenException
import com.yapp.breake.data.model.NetworkException.InternalServerErrorException
import com.yapp.breake.data.model.NetworkException.NotFoundException
import com.yapp.breake.data.model.NetworkException.UnauthorizedException
import com.yapp.breake.data.model.NetworkException.UnknownNetworkException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeoutException

/** 네트워크 API를 안전하게 호출하고 응답을 원하는 타입으로 매핑하는 함수입니다.
 *
 * @param apiCall 네트워크 호출을 수행하는 suspend 함수입니다.
 * @param mapper 성공한 응답 데이터를 원하는 타입으로 매핑하는 함수입니다.
 * @param predicateOnSuccess 데이터가 특정 조건을 만족하는지 확인하는 조건 함수입니다.
 * @return 네트워크 호출 결과(Success, Error, Exception)를 방출하는 Flow입니다.
 */
internal fun <T, R> safeNetworkCall(
	apiCall: suspend () -> ApiResponse<T>,
	mapper: (T) -> R,
	predicateOnSuccess: (T) -> Boolean = { true },
): Flow<ResponseResult<R>> = flow {
	when (val response = apiCall()) {
		is ApiResponse.Success -> {
			if (predicateOnSuccess(response.data)) {
				emit(ResponseResult.Success(mapper(response.data)))
			} else {
				emit(ResponseResult.Error("해당 요청에 문제가 있습니다"))
			}
		}

		is ApiResponse.Failure.Error -> {
			emit(ResponseResult.Error(response.message()))
		}

		is ApiResponse.Failure.Exception -> {
			val exception = when (val throwable = response.throwable) {
				is retrofit2.HttpException -> {
					when (throwable.code()) {
						401 -> UnauthorizedException("인증이 필요합니다")
						403 -> ForbiddenException("접근이 금지되었습니다")
						404 -> NotFoundException("리소스를 찾을 수 없습니다")
						500 -> InternalServerErrorException("서버 내부 오류가 발생했습니다")
						else -> UnknownNetworkException("알 수 없는 오류가 발생했습니다")
					}
				}
				is java.net.UnknownHostException -> UnknownNetworkException("서버에 연결할 수 없습니다")
				is java.net.SocketTimeoutException -> TimeoutException("요청 시간이 초과되었습니다")
				else -> throwable
			}
			emit(ResponseResult.Exception(exception))
		}
	}
}
