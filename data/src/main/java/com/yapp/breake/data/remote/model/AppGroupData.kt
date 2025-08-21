package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppGroupData(
	val groupId: Long,
	val name: String,
	val groupApps: List<GroupApp>,
)
