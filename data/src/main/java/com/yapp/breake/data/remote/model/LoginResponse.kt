package com.yapp.breake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
	@SerialName("data") val data: LoginToken,
) : BaseResponse(code = 0)
