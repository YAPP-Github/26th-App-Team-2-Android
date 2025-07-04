package com.yapp.breake.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yapp.breake.notification.NotificationReceiver
import timber.log.Timber

object AlarmScheduler {

	private const val TAG = "AlarmScheduler"
	const val ACTION_SHOW_NOTIFICATION = "com.example.myapplication.scheduler.action.SHOW_NOTIFICATION"
	const val EXTRA_NOTIFICATION_ID = "notification_id"
	const val EXTRA_NOTIFICATION_TITLE = "notification_title"
	const val EXTRA_NOTIFICATION_MESSAGE = "notification_message"

	fun scheduleNotification(
		context: Context,
		timeInMillis: Long,
		notificationId: Int,
		title: String,
		message: String,
	): Boolean { // Boolean 반환 타입 추가
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = ACTION_SHOW_NOTIFICATION
			putExtra(EXTRA_NOTIFICATION_ID, notificationId)
			putExtra(EXTRA_NOTIFICATION_TITLE, title)
			putExtra(EXTRA_NOTIFICATION_MESSAGE, message)
		}

		val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		} else {
			PendingIntent.FLAG_UPDATE_CURRENT
		}
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			notificationId,
			intent,
			pendingIntentFlags,
		)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
			Timber.w("정확한 알람 권한이 없습니다. ID: $notificationId 에 대한 정확한 알람을 예약할 수 없습니다.")
			return false // 설정 실패 반환
		}

		try {
			alarmManager.setExactAndAllowWhileIdle(
				AlarmManager.RTC_WAKEUP,
				timeInMillis,
				pendingIntent,
			)
			Timber.i("ID: $notificationId 에 대한 알람이 $timeInMillis 에 성공적으로 예약되었습니다.")
			return true // 설정 성공 반환
		} catch (se: SecurityException) {
			Timber.e("SecurityException: ID: $notificationId 에 대한 정확한 알람을 예약할 수 없습니다. $se")
			return false // 설정 실패 반환
		}
	}

	fun cancelNotification(context: Context, notificationId: Int) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = ACTION_SHOW_NOTIFICATION // 스케줄 시 사용한 것과 동일한 action
		}
		val pendingIntentFlags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
		} else {
			PendingIntent.FLAG_NO_CREATE
		}
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			notificationId,
			intent,
			pendingIntentFlags,
		)
		// PendingIntent가 null이 아닐 경우에만 (즉, 이전에 생성된 알람이 있을 경우) 취소
		pendingIntent?.let {
			alarmManager.cancel(it)
			it.cancel() // PendingIntent 자체도 취소
		}
	}
}
