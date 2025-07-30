package com.yapp.breake.data.mapper

import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.database.entity.GroupEntity
import com.yapp.breake.core.database.entity.SnoozeEntity
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.Snooze
import com.yapp.breake.core.util.toByteArray
import com.yapp.breake.core.util.toDrawable

internal fun AppGroupEntity.toAppGroup(): AppGroup {
	return AppGroup(
		id = group.groupId,
		name = group.name,
		appGroupState = group.appGroupState,
		apps = apps.map(AppEntity::toApp),
		snoozes = snoozes.map(SnoozeEntity::toSnooze),
		endTime = group.endTime,
	)
}

internal fun AppGroup.toGroupEntity(): GroupEntity = GroupEntity(
	groupId = id,
	name = name,
	appGroupState = appGroupState,
	endTime = endTime,
)

internal fun AppEntity.toApp(): App {
	return App(
		packageName = packageName,
		name = name,
		icon = icon?.toByteArray(),
		category = category,
	)
}

internal fun App.toAppEntity(parentGroupId: Long): AppEntity {
	return AppEntity(
		packageName = packageName,
		name = name,
		icon = icon?.toDrawable(),
		category = category,
		parentGroupId = parentGroupId,
	)
}

internal fun SnoozeEntity.toSnooze(): Snooze {
	return Snooze(
		startTime = snoozeTime,
	)
}
