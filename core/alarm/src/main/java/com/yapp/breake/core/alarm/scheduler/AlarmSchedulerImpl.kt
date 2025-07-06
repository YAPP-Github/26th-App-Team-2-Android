package com.yapp.breake.core.alarm.scheduler

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.yapp.breake.core.alarm.notification.NotificationReceiver
import com.yapp.breake.core.alarm.scheduler.AlarmScheduler.Companion.EXTRA_ALARM_ID
import com.yapp.breake.core.common.IntentConstants
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
	private val context: Context,
) : AlarmScheduler {

	@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
	override fun scheduleAlarm(
		alarmId: Int,
		minute: Int,
	): Boolean {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = IntentConstants.ACTION_SHOW_NOTIFICATION
			putExtra(EXTRA_ALARM_ID, alarmId)
		}

		val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			alarmId,
			intent,
			pendingIntentFlags,
		)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
			Timber.w("정확한 알람 권한이 없습니다. ID: $alarmId 에 대한 정확한 알람을 예약할 수 없습니다.")
			return false
		}

		try {
			val startTime = LocalDateTime.now()
			val triggerTime = startTime.plusMinutes(minute.toLong())
			val triggerAtMillis =
				triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
			alarmManager.setExactAndAllowWhileIdle(
				AlarmManager.RTC_WAKEUP,
				triggerAtMillis,
				pendingIntent,
			)
			Timber.i("ID: $alarmId 에 대한 알람이 $triggerAtMillis 에 성공적으로 예약되었습니다.")
			return true
		} catch (se: SecurityException) {
			Timber.e("SecurityException: ID: $alarmId 에 대한 정확한 알람을 예약할 수 없습니다. $se")
			return false
		}
	}

	override fun cancelAlarm(notificationId: Int) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = IntentConstants.ACTION_SHOW_NOTIFICATION
		}
		val pendingIntentFlags = PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			notificationId,
			intent,
			pendingIntentFlags,
		)

		pendingIntent?.let {
			alarmManager.cancel(it)
			it.cancel() // PendingIntent 자체도 취소
		}
	}
}

