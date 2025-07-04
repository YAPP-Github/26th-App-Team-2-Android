package com.yapp.breake.data.api.model

import com.yapp.breake.data.api.ApiConfig
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
	val provider: String,
	val authorizationCode: String,
	val deviceId: String = ApiConfig.AndroidID.deviceInfo,
)
