package com.yapp.breake.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
	@SerialName("code") val code: Int,
	@SerialName("data") val data: LoginToken,
)
