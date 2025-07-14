package com.yapp.breake.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
	val accessToken: String,
	val refreshToken: String,
	val status: UserTokenStatus,
) {
	companion object {
		val EMPTY = UserToken(
			accessToken = "",
			refreshToken = "",
			status = UserTokenStatus.INACTIVE,
		)
	}
}
