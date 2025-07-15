package com.yapp.breake.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MemberData(
	@SerialName("nickname") val nickname: String,
	@SerialName("state") val state: String,
)
