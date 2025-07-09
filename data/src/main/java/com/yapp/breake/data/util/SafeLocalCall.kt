package com.yapp.breake.data.util

import com.yapp.breake.data.model.LocalException.DataEmptyException
import com.yapp.breake.data.model.LocalException.UnknownException

suspend inline fun <T> safeLocalCall(
	crossinline localCall: suspend () -> T,
): T {
	return try {
		localCall()
	} catch (exception: Exception) {
		val message = exception.message ?: "Unknown error"

		throw when (exception) {
			is IllegalArgumentException -> DataEmptyException(message)
			else -> UnknownException(message)
		}
	}
}
