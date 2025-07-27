package com.yapp.breake.domain.repository

import com.yapp.breake.core.common.AlarmAction
import java.time.LocalDateTime

interface AlarmScheduler {

	fun scheduleAlarm(
		groupId: Long,
		appName: String,
		second: Int = 0,
		action: AlarmAction,
	): Result<LocalDateTime>

	fun cancelAlarm(
		groupId: Long,
		action: AlarmAction,
	)
}
