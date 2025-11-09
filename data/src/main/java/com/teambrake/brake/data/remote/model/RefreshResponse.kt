package com.teambrake.brake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshResponse(
	@SerialName("data") val data: RefreshToken,
) : BaseResponse(status = 0)
