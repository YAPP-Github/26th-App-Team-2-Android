package com.yapp.breake.domain.repository

import com.yapp.breake.core.common.AlarmAction

interface AlarmScheduler {

	fun scheduleAlarm(
		groupId: Long,
		second: Int = 0,
		action: AlarmAction,
	): Result<Unit>

	fun cancelAlarm(
		groupId: Long,
	)
}
