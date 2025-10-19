package com.teambrake.brake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshToken(
	val accessToken: String,
	val refreshToken: String,
)
