package com.yapp.breake.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class MemberResponse(
	val code: Int,
	val data: MemberData,
)
