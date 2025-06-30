package com.yapp.breake.core.database.temp

import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.database.entity.SnoozeEntity
import com.yapp.breake.core.model.app.App
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.Snooze

internal fun AppGroupEntity.toAppGroup(): AppGroup {
	return AppGroup(
		id = group.groupId,
		name = group.name,
		apps = apps.map { it.toApp() },
		snoozes = snoozes.map { it.toSnooze() }
	)
}

internal fun AppEntity.toApp(): App {
	return App(
		packageName = packageName,
		category = category
	)
}

internal fun SnoozeEntity.toSnooze(): Snooze {
	return Snooze(
		startTime = snoozeTime,
	)
}
