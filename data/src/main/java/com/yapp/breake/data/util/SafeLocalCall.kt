package com.yapp.breake.data.util

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.data.model.LocalException.DataEmptyException
import com.yapp.breake.data.model.LocalException.UnknownLocalException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

/**
 * 로컬 데이터 소스에서 데이터를 안전하게 가져오는 함수입니다.
 *
 * @param localCall 로컬 데이터 소스에서 데이터를 가져오는 Flow입니다.
 * @param mapper 데이터를 매핑하는 함수입니다. 이 함수는 T 타입의 데이터를 R 타입으로 변환합니다.
 * @param predicateOnSuccess 조건 함수로, 해당 조건을 충족하는 경우에만 Success, 실패 시 Error를 반환합니다.
 * @return 성공 시 매핑된 데이터를 포함하는 Success, 실패 시 Error 또는 Exception을 포함하는 Flow입니다.
 */
internal fun <T, R> safeLocalCall(
	localCall: Flow<T>,
	mapper: (T) -> R,
	predicateOnSuccess: (T) -> Boolean = { true },
): Flow<ResponseResult<R>> {
	return localCall
		.map { data ->
			if (predicateOnSuccess(data)) {
				ResponseResult.Success(mapper(data))
			} else {
				ResponseResult.Error(
					"앱에 저장된 데이터가 유효하지 않습니다.\n" +
						"앱을 재시작하거나, 캐시를 삭제해주세요.",
				)
			}
		}
		.catch { e ->
			when (e) {
				is IOException -> {
					emit(ResponseResult.Exception(DataEmptyException("앱에 저장된 데이터가 없습니다.")))
				}

				else -> {
					emit(ResponseResult.Exception(UnknownLocalException("알 수 없는 오류가 발생했습니다.")))
				}
			}
		}
}
