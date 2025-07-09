package com.yapp.breake.data.util

import com.yapp.breake.data.model.NetworkException
import retrofit2.HttpException

suspend inline fun <T> safeNetworkCall(
	crossinline apiCall: suspend () -> T,
): T {
	return try {
		apiCall()
	} catch (httpException: HttpException) {
		val message = httpException.message() ?: "Unknown error"

		throw when (httpException.code()) {
			401 -> NetworkException.UnauthorizedException(message)
			else -> NetworkException.UnknownNetworkException(message)
		}
	}
}
