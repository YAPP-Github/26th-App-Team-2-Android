package com.yapp.breake.core.alarm.scheduler

interface AlarmScheduler {

	fun scheduleAlarm(
		alarmId: Long,
		minute: Int,
	): Result<Unit>

	fun cancelAlarm(
		notificationId: Int,
	)

	companion object {
		const val EXTRA_ALARM_ID = "alarm_id"
	}
}
