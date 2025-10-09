package com.yapp.breake.core.detection

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedDatabaseImpl @Inject constructor() : CachedDatabase {
	private val _cachedAppGroups: MutableStateFlow<List<CachedTargetAppGroup>> =
		MutableStateFlow(emptyList())
	override val cachedAppGroups: StateFlow<List<CachedTargetAppGroup>> = _cachedAppGroups.asStateFlow()

	override fun initializeCachedState(appGroups: List<AppGroup>) {
		_cachedAppGroups.value = appGroups.map {
			CachedTargetAppGroup(
				apps = it.apps.map { app ->
					CachedTargetApp(
						packageName = app.packageName,
						name = app.name,
						category = app.category,
					)
				},
				groupId = it.id,
				groupName = it.name,
				groupState = it.appGroupState,
				snoozesCount = it.snoozesCount,
			)
		}
	}

	override fun addAppGroupToCache(appGroup: AppGroup) {
		_cachedAppGroups.value = _cachedAppGroups.value + CachedTargetAppGroup(
			apps = appGroup.apps.map { app ->
				CachedTargetApp(
					packageName = app.packageName,
					name = app.name,
					category = app.category,
				)
			},
			groupId = appGroup.id,
			groupName = appGroup.name,
			groupState = appGroup.appGroupState,
			snoozesCount = appGroup.snoozesCount,
		)
	}

	override fun updateAppGroupInCache(appGroup: AppGroup) {
		_cachedAppGroups.value = _cachedAppGroups.value.map {
			if (it.groupId == appGroup.id) {
				CachedTargetAppGroup(
					apps = appGroup.apps.map { app ->
						CachedTargetApp(
							packageName = app.packageName,
							name = app.name,
							category = app.category,
						)
					},
					groupId = appGroup.id,
					groupName = appGroup.name,
					groupState = appGroup.appGroupState,
					snoozesCount = appGroup.snoozesCount,
				)
			} else {
				it
			}
		}
	}

	override fun updateCachedState(
		groupId: Long,
		appGroupState: AppGroupState,
	) {
		_cachedAppGroups.value = _cachedAppGroups.value.map {
			if (it.groupId == groupId) {
				it.copy(groupState = appGroupState)
			} else {
				it
			}
		}
	}

	override fun updateSnoozeCount(groupId: Long, snoozesCount: Int) {
		_cachedAppGroups.value = _cachedAppGroups.value.map {
			if (it.groupId == groupId) {
				it.copy(snoozesCount = snoozesCount)
			} else {
				it
			}
		}
	}

	override fun removeAppGroupFromCache(groupId: Long) {
		_cachedAppGroups.value = _cachedAppGroups.value.filter { it.groupId != groupId }
	}

	override fun clearCache() {
		_cachedAppGroups.value = emptyList()
	}
}
