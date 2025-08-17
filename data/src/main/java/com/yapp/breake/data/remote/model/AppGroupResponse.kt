package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppGroupResponse(
	val data: AppGroupData,
) : BaseResponse(code = 0)

@Serializable
internal data class AppGroupData(
	val groupId: Long,
	val name: String,
	val groupApps: List<GroupApp>,
)

@Serializable
internal data class GroupApp(
	val groupAppId: Long,
	val name: String,
)
