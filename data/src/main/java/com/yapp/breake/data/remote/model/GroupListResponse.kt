package com.yapp.breake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class GroupListResponse(
	val data: GroupListData,
) : BaseResponse(status = 0)

@Serializable
internal data class GroupListData(
	val groups: List<AppGroupData>,
)
