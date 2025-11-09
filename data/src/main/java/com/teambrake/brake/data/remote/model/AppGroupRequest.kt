package com.teambrake.brake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppGroupRequest(
	val name: String,
	val groupApps: List<AppRequest>,
)
