package com.teambrake.brake.core.model.user.exception

sealed class LocalException(override val message: String) : Exception(message) {
	data class DataStoreEmptyException(
		override val message: String = "DataStore is empty",
	) : LocalException(message)
}
