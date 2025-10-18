package com.teambrake.brake.domain.repository

import com.teambrake.brake.core.common.AlarmAction
import java.time.LocalDateTime

interface AlarmScheduler {

	fun scheduleAlarm(
		groupId: Long,
		groupName: String,
		triggerTime: LocalDateTime,
		action: AlarmAction,
	): Result<LocalDateTime>

	fun cancelAlarm(
		groupId: Long,
		action: AlarmAction,
	)
}
