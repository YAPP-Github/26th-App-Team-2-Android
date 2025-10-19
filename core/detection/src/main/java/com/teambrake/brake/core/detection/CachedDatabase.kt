package com.teambrake.brake.core.detection

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.StateFlow

interface CachedDatabase {
	val cachedAppGroups: StateFlow<List<CachedTargetAppGroup>>
	fun initializeCachedState(appGroups: List<AppGroup>)
	fun addAppGroupToCache(appGroup: AppGroup)
	fun updateAppGroupInCache(appGroup: AppGroup)
	fun updateCachedState(groupId: Long, appGroupState: AppGroupState)
	fun updateSnoozeCount(groupId: Long, snoozesCount: Int)
	fun removeAppGroupFromCache(groupId: Long)
	fun clearCache()
}
