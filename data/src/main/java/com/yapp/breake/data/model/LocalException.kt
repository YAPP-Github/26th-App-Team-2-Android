package com.yapp.breake.data.model

sealed class LocalException(override val message: String) : Exception(message) {

	data class DataEmptyException(
		override val message: String = "데이터가 비어있습니다.",
	) : LocalException(message)

	data class UnknownException(
		override val message: String = "알 수 없는 오류가 발생했습니다.",
	) : LocalException(message)
}
