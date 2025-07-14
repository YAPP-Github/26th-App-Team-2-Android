package com.yapp.breake.core.datastore.model

import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.core.model.user.UserTokenStatus.INACTIVE
import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUserToken(
	val accessToken: String,
	val refreshToken: String,
	val status: UserTokenStatus,
) {
	companion object {
		val Empty = DatastoreUserToken(
			accessToken = "",
			refreshToken = "",
			status = INACTIVE,
		)
	}
}
