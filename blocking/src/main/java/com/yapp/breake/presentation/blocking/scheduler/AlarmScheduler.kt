package com.yapp.breake.presentation.blocking.scheduler

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresPermission
import com.yapp.breake.presentation.blocking.notification.NotificationReceiver
import com.yapp.breake.core.common.IntentConstants
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId

object AlarmScheduler {

	const val EXTRA_ALARM_ID = "alarm_id"

	@RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
	fun scheduleAlarm(
		context: Context,
		alarmId: Long,
		minute: Int,
	): Result<Unit> {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = IntentConstants.ACTION_ALARM_FINISH
			putExtra(EXTRA_ALARM_ID, alarmId)
		}

		val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			alarmId.toInt(),
			intent,
			pendingIntentFlags,
		)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
			Timber.w("정확한 알람 권한이 없습니다. ID: $alarmId 에 대한 정확한 알람을 예약할 수 없습니다.")
			return Result.failure(SecurityException("정확한 알람 권한이 없습니다."))
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
			return Result.success(Unit) // 성공적으로 예약됨
		} catch (se: SecurityException) {
			Timber.e("SecurityException: ID: $alarmId 에 대한 정확한 알람을 예약할 수 없습니다. $se")
			return Result.failure(se)
		}
	}

	fun cancelAlarm(
		context: Context,
		notificationId: Int,
	) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = IntentConstants.ACTION_ALARM_FINISH
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
