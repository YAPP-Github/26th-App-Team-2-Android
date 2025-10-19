package com.teambrake.brake.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AppGroupResponse(
	val data: AppGroupData,
) : BaseResponse(status = 0)
