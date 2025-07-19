package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.AppGroupState

interface AlarmScheduler {

	suspend fun scheduleAlarm(
		groupId: Long,
		appGroupState: AppGroupState,
		second: Int = 0,
	): Result<Unit>

	suspend fun cancelAlarm(
		groupId: Long,
	)
}
