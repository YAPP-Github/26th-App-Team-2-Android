package com.yapp.breake.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginToken(
	@SerialName("accessToken") val accessToken: String,
	@SerialName("refreshToken") val refreshToken: String,
	@SerialName("memberState") val memberState: String,
)
