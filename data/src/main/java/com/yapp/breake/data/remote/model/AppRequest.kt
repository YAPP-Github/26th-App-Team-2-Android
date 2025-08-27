package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppRequest(
	val name: String,
	val packageName: String,
	val groupAppId: Long? = null,
)
