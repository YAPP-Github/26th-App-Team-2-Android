package com.yapp.breake.core.alarm.scheduler

import androidx.annotation.RequiresPermission

interface AlarmScheduler {

	@RequiresPermission(android.Manifest.permission.SCHEDULE_EXACT_ALARM)
	fun scheduleAlarm(
		alarmId: Int,
		minute: Int,
	): Boolean

	fun cancelAlarm(notificationId: Int)

	companion object {
		const val EXTRA_ALARM_ID = "alarm_id"
	}
}

