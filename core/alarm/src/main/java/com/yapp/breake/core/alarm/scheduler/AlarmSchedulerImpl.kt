package com.yapp.breake.core.alarm.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yapp.breake.core.alarm.notification.NotificationReceiver
import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.domain.repository.AlarmScheduler
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
	private val alarmManager: AlarmManager,
	private val context: Context,
) : AlarmScheduler {

	override fun scheduleAlarm(
		groupId: Long,
		appName: String,
		triggerTime: LocalDateTime,
		action: AlarmAction,
	): Result<LocalDateTime> {
		if (!canScheduleExactAlarms()) {
			val errorMessage = "정확한 알람 권한이 없습니다. ID: $groupId 에 대한 정확한 알람을 예약할 수 없습니다."
			Timber.w(errorMessage)
			return Result.failure(SecurityException("정확한 알람 권한이 없습니다."))
		}

		val intent = getPendingIntent(groupId, appName, action.name)
		Timber.d("$triggerTime 에 알람을 예약합니다. ID: $groupId, 액션: ${action.name}")

		return try {
			scheduleAlarm(triggerTime, intent)
			Result.success(triggerTime)
		} catch (se: SecurityException) {
			Timber.e("SecurityException: ID: $groupId 에 대한 정확한 알람을 예약할 수 없습니다. $se")
			Result.failure(se)
		}
	}

	override fun cancelAlarm(groupId: Long, action: AlarmAction) {
		val intent = getPendingIntent(groupId, "", action.name)

		intent.let {
			alarmManager.cancel(it)
			it.cancel()
		}
	}

	private fun canScheduleExactAlarms(): Boolean {
		return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
			alarmManager.canScheduleExactAlarms()
		} else {
			true
		}
	}

	private fun getPendingIntent(
		groupId: Long,
		appName: String,
		intentAction: String,
	): PendingIntent {
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = intentAction
			putExtra(EXTRA_GROUP_ID, groupId)
			putExtra(EXTRA_APP_NAME_ID, appName)
		}

		val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		return PendingIntent.getBroadcast(
			context,
			groupId.toInt(),
			intent,
			pendingIntentFlags,
		)
	}

	@SuppressLint("MissingPermission")
	private fun scheduleAlarm(
		triggerTime: LocalDateTime,
		pendingIntent: PendingIntent,
	) {
		val triggerAtMillis = triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
		alarmManager.setExactAndAllowWhileIdle(
			AlarmManager.RTC_WAKEUP,
			triggerAtMillis,
			pendingIntent,
		)
	}

	companion object {
		const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
		const val EXTRA_APP_NAME_ID = "EXTRA_APP_NAME_ID"
	}
}
