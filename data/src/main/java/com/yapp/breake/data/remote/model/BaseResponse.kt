package com.yapp.breake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
	@SerialName("code") val code: Int = 0,
)
