package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppGroupRequest(
	val name: String,
	val groupApps: List<AppRequest>,
)

@Serializable
internal data class AppRequest(
	val name: String,
	val groupAppId: Long? = null,
)
