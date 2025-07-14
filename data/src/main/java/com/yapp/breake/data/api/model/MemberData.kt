package com.yapp.breake.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberData(
	val nickname: String,
	val state: String,
)
