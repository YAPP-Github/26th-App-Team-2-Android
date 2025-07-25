package com.yapp.breake.data.mapper

import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.database.entity.SnoozeEntity
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.Snooze
import com.yapp.breake.core.util.toByteArray

internal fun AppGroupEntity.toAppGroup(): AppGroup {
	return AppGroup(
		id = group.groupId,
		name = group.name,
		appGroupState = group.appGroupState,
		apps = apps.map(AppEntity::toApp),
		snoozes = snoozes.map(SnoozeEntity::toSnooze),
	)
}

internal fun AppEntity.toApp(): App {
	return App(
		packageName = packageName,
		name = name,
		icon = icon?.toByteArray(),
		category = category,
	)
}

internal fun SnoozeEntity.toSnooze(): Snooze {
	return Snooze(
		startTime = snoozeTime,
	)
}

