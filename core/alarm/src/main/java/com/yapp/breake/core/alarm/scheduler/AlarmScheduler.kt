package com.yapp.breake.core.alarm.scheduler

import com.yapp.breake.core.model.app.AppGroupState

interface AlarmScheduler {

	suspend fun scheduleAlarm(
		groupId: Long,
		appGroupState: AppGroupState,
		minute: Int = 0,
	): Result<Unit>

	suspend fun cancelAlarm(
		groupId: Long,
	)
}
