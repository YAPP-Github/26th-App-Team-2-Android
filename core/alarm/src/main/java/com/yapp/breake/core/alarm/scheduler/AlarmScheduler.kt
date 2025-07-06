package com.yapp.breake.core.alarm.scheduler

import androidx.annotation.RequiresPermission
import java.time.LocalDateTime

interface AlarmScheduler {

	@RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
	fun scheduleAlarm(
		alarmId: Int,
		minute: Int,
		startTime: LocalDateTime,
	): Boolean

	fun cancelAlarm(notificationId: Int)

	companion object {
		const val EXTRA_ALARM_ID = "alarm_id"
	}
}

