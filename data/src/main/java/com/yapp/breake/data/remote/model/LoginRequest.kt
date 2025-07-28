package com.yapp.breake.data.remote.model

import com.yapp.breake.data.remote.retrofit.ApiConfig
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
	val provider: String,
	val authorizationCode: String,
	val deviceName: String = ApiConfig.AndroidID.deviceInfo,
)
