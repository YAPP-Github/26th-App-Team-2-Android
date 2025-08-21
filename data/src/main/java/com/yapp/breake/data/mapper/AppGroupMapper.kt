package com.yapp.breake.data.mapper

import com.yapp.breake.core.appscanner.InstalledAppScanner
import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.database.entity.GroupEntity
import com.yapp.breake.core.database.entity.SnoozeEntity
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.model.app.Snooze
import com.yapp.breake.core.util.toByteArray
import com.yapp.breake.data.remote.model.AppGroupData
import com.yapp.breake.data.remote.model.AppGroupRequest
import com.yapp.breake.data.remote.model.AppRequest

internal fun AppGroupEntity.toAppList(appScanner: InstalledAppScanner): AppGroup {
	return AppGroup(
		id = group.groupId,
		name = group.name,
		appGroupState = group.appGroupState,
		apps = apps.map {
			it.toApp(appScanner)
		},
		snoozes = snoozes.map(SnoozeEntity::toSnooze),
		goalMinutes = group.goalMinutes,
		sessionStartTime = group.sessionStartTime,
		startTime = group.startTime,
		endTime = group.endTime,
	)
}

internal fun AppGroup.toGroupEntity(): GroupEntity = GroupEntity(
	groupId = id,
	name = name,
	appGroupState = appGroupState,
	goalMinutes = goalMinutes,
	sessionStartTime = sessionStartTime,
	startTime = startTime,
	endTime = endTime,
)

internal fun AppEntity.toApp(appScanner: InstalledAppScanner): App {
	return App(
		packageName = packageName,
		id = id,
		name = name,
		icon = appScanner.getIconDrawable(packageName).toByteArray(),
		category = category,
	)
}

internal fun App.toAppEntity(parentGroupId: Long): AppEntity {
	return AppEntity(
		packageName = packageName,
		id = id,
		name = name,
		category = category,
		parentGroupId = parentGroupId,
	)
}

internal fun SnoozeEntity.toSnooze(): Snooze {
	return Snooze(
		startTime = snoozeTime,
	)
}

internal fun AppGroup.toAppGroupRequest(): AppGroupRequest {
	return AppGroupRequest(
		name = this.name,
		groupApps = this.apps.map { app ->
			AppRequest(
				name = app.name,
				groupAppId = app.id,
			)
		},
	)
}

internal fun AppGroupData.toAppGroup(): AppGroup {
	return AppGroup(
		id = groupId,
		name = name,
		appGroupState = AppGroupState.NeedSetting,
		apps = groupApps.map { groupApp ->
			App(
				id = groupApp.groupAppId,
				name = groupApp.name,
				packageName = groupApp.packageName,
				icon = null,
				category = "",
			)
		},
		snoozes = emptyList(),
		goalMinutes = null,
		sessionStartTime = null,
		startTime = null,
		endTime = null,
	)
}
