package com.teambrake.brake.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
	val accessToken: String,
	val refreshToken: String,
	val status: UserStatus,
)
