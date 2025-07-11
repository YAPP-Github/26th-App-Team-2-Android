package com.yapp.breake.data.util

import com.skydoves.sandwich.ApiResponse
import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.data.model.NetworkException.NetworkForbiddenException
import com.yapp.breake.data.model.NetworkException.HttpRequestException
import com.yapp.breake.data.model.NetworkException.NetworkInternalServerErrorException
import com.yapp.breake.data.model.NetworkException.NetworkConnectionException
import com.yapp.breake.data.model.NetworkException.NetworkIOException
import com.yapp.breake.data.model.NetworkException.NetworkTimeoutException
import com.yapp.breake.data.model.NetworkException.NetworkNotFoundException
import com.yapp.breake.data.model.NetworkException.NetworkUnauthorizedException
import com.yapp.breake.data.model.NetworkException.NetworkUnknownException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
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
			val retrofitResponse = response.payload as? Response<*>
			val code = retrofitResponse?.code()

			val exception = when (code) {
				401 -> NetworkUnauthorizedException("인증이 필요합니다")
				403 -> NetworkForbiddenException("접근이 금지되었습니다")
				404 -> NetworkNotFoundException("리소스를 찾을 수 없습니다")
				500 -> NetworkInternalServerErrorException("서버 내부 오류가 발생했습니다")
				else -> NetworkUnknownException("알 수 없는 네트워크 오류가 발생했습니다")
			}
			emit(ResponseResult.Exception(exception))
		}

		is ApiResponse.Failure.Exception -> {
			val exception = when (response.throwable) {
				// 네트워크 관련 예외
				is java.net.UnknownHostException -> NetworkConnectionException("서버에 연결할 수 없습니다")
				is java.net.SocketTimeoutException -> NetworkTimeoutException("요청 시간이 초과되었습니다")
				is java.net.ConnectException -> NetworkConnectionException("서버 연결에 실패했습니다")
				is java.net.SocketException -> NetworkConnectionException("네트워크 연결에 문제가 발생했습니다")
				is java.io.IOException -> NetworkIOException("네트워크 입출력 오류가 발생했습니다")

				// Retrofit/OkHttp 관련 예외
				is retrofit2.HttpException -> HttpRequestException("HTTP 오류가 발생했습니다")
				is TimeoutException -> NetworkTimeoutException("요청 시간이 초과되었습니다")

				else -> NetworkUnknownException("알 수 없는 네트워크 오류가 발생했습니다")
			}
			emit(ResponseResult.Exception(exception))
		}
	}
}
