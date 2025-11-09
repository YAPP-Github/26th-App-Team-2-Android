package com.teambrake.brake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GroupApp(
	val groupAppId: Long,
	val name: String,
	val packageName: String = "",
)
