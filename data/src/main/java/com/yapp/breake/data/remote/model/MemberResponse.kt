package com.yapp.breake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
	@SerialName("code") val code: Int,
	@SerialName("data") val data: MemberData,
)
