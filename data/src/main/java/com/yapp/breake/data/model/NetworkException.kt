package com.yapp.breake.data.model

sealed class NetworkException(override val message: String) : Exception(message) {

	data class UnauthorizedException(
		override val message: String = "인증되지 않은 사용자입니다.",
	) : NetworkException(message)

	data class UnknownNetworkException(
		override val message: String = "알 수 없는 네트워크 오류가 발생했습니다.",
	) : NetworkException(message)
}
