package com.yapp.breake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUserToken(
	val accessToken: String,
	val refreshToken: String,
) {
	companion object {
		val Empty = DatastoreUserToken(
			accessToken = "",
			refreshToken = "",
		)
	}
}
