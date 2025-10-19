package com.teambrake.brake.core.detection

import com.teambrake.brake.core.model.app.AppGroupState

data class CachedTargetApp(
	val packageName: String,
	val name: String,
	val category: String,
)

data class CachedTargetAppGroup(
	val apps: List<CachedTargetApp>,
	val groupId: Long,
	val groupName: String,
	val groupState: AppGroupState,
	val snoozesCount: Int,
)
