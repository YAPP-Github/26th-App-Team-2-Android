package com.teambrake.brake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
	@SerialName("data") val data: MemberData,
) : BaseResponse(status = 0)
