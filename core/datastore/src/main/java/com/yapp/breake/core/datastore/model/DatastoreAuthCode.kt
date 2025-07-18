package com.yapp.breake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreAuthCode(val authCode: String?) {
	companion object {
		val Empty = DatastoreAuthCode(authCode = null)
	}
}
