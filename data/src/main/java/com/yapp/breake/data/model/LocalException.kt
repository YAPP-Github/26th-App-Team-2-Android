package com.yapp.breake.data.model

sealed class LocalException(override val message: String) : Exception(message) {

	data class DataEmptyException(
		override val message: String = "Empty Data",
	) : LocalException(message)

	data class UnknownLocalException(
		override val message: String = "Unknown Local Error",
	) : LocalException(message)
}
