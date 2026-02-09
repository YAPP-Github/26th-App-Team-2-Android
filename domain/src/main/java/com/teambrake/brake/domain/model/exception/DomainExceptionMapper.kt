package com.teambrake.brake.domain.model.exception

import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.HttpUnsuccessfulCodeError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.RemoteServerNotReachedError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError

/**
 * Domain Exception을 ApiCallError로 변환
 * UseCase 레이어에서 사용
 */
fun Throwable.toApiCallError(): ApiCallError = when (this) {
	is HttpException -> HttpUnsuccessfulCodeError(code)
	is LocalException -> LocalApiCallError(this)
	is NetworkException -> RemoteServerNotReachedError
	is UnknownException -> UndefinedExceptionError(this)
	else -> UndefinedExceptionError(this)
}
