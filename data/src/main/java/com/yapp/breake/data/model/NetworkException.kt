package com.yapp.breake.data.model

sealed class NetworkException(override val message: String) : Exception(message) {

	data class UnauthorizedException(
		override val message: String = "Not authorized",
	) : NetworkException(message)

	data class ForbiddenException(
		override val message: String = "Forbidden",
	) : NetworkException(message)

	data class NotFoundException(
		override val message: String = "Not Found",
	) : NetworkException(message)

	data class InternalServerErrorException(
		override val message: String = "Internal Server Error",
	) : NetworkException(message)

	data class UnknownNetworkException(
		override val message: String = "Unknown Network Error",
	) : NetworkException(message)
}
