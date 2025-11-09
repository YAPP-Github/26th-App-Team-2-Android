package com.teambrake.brake.core.datastore.model

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.core.model.user.UserStatus.INACTIVE
import kotlinx.serialization.Serializable

@Serializable
data class DatastoreUserToken(
	val accessToken: String?,
	val refreshToken: String?,
	val status: UserStatus,
) {
	companion object {
		val Empty = DatastoreUserToken(
			accessToken = null,
			refreshToken = null,
			status = INACTIVE,
		)
	}
}
