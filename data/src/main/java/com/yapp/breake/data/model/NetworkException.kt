package com.yapp.breake.data.model

sealed class NetworkException(override val message: String) : Exception(message) {

	data class NetworkUnauthorizedException(
		override val message: String = "Not authorized",
	) : NetworkException(message)

	data class NetworkForbiddenException(
		override val message: String = "Forbidden",
	) : NetworkException(message)

	data class NetworkNotFoundException(
		override val message: String = "Not Found",
	) : NetworkException(message)

	data class NetworkInternalServerErrorException(
		override val message: String = "Internal Server Error",
	) : NetworkException(message)

	data class NetworkTimeoutException(
		override val message: String = "Network Timeout",
	) : NetworkException(message)

	data class HttpRequestException(
		override val message: String = "HTTP Request Error",
	) : NetworkException(message)

	data class NetworkIOException(
		override val message: String = "Network IO Error",
	) : NetworkException(message)

	data class NetworkConnectionException(
		override val message: String = "Network Connection Error",
	) : NetworkException(message)

	data class NetworkUnknownException(
		override val message: String = "Unknown Network Error",
	) : NetworkException(message)
}
