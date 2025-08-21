package com.yapp.breake.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponse(
	@SerialName("status") val status: Int = 0,
)
