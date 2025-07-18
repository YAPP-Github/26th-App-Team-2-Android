package com.yapp.breake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUserInfo(
	val nickname: String?,
	val imageUrl: String?,
) {
	companion object {
		val Empty = DatastoreUserInfo(nickname = null, imageUrl = null)
	}
}
