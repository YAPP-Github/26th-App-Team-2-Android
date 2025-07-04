package com.yapp.breake.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yapp.breake.scheduler.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

	private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

	companion object {
		private const val CHANNEL_ID = "ScheduledNotificationChannel"
		const val ACTION_SHOW_REST_OVERLAY = "com.example.myapplication.action.SHOW_REST_OVERLAY"
		private const val TAG = "NotificationReceiver"
	}

	override fun onReceive(context: Context, intent: Intent) {
		Timber.d("onReceive 호출됨, action: ${intent.action}")
		if (intent.action == AlarmScheduler.ACTION_SHOW_NOTIFICATION) {
			Timber.d("${AlarmScheduler.ACTION_SHOW_NOTIFICATION} 수신됨.")

			// ScreenTimeService 중지
			com.yapp.breake.core.service.ScreenTimeService.stopService(context)
			Timber.d("ScreenTimeService.stopService 호출됨.")

			scope.launch {
//                EventBus.postOverlayEvent(OverlayEvent.ShowRestOverlay)
				Timber.d("EventBus.postOverlayEvent(OverlayEvent.ShowRestOverlay) 전송됨.")
			}

			// 사용자에게 직접적인 알림 표시는 선택 사항 (이미 오버레이가 표시될 것이므로)
			// val notificationId = intent.getIntExtra(AlarmScheduler.EXTRA_NOTIFICATION_ID, 0)
			// val title = intent.getStringExtra(AlarmScheduler.EXTRA_NOTIFICATION_TITLE) ?: "알림"
			// val message = intent.getStringExtra(AlarmScheduler.EXTRA_NOTIFICATION_MESSAGE) ?: "예약된 알림입니다."
			// createNotificationChannel(context)
			// val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			// val notification = NotificationCompat.Builder(context, CHANNEL_ID) ... .build()
			// try {
			// notificationManager.notify(notificationId, notification)
			// } catch (se: SecurityException) {
			// println("SecurityException: Cannot post notification. $se")
			// }
		}
	}

	private fun createNotificationChannel(context: Context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val name = "Scheduled Notifications"
			val descriptionText = "Channel for scheduled notifications"
			val importance = NotificationManager.IMPORTANCE_DEFAULT
			val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
				description = descriptionText
			}
			val notificationManager: NotificationManager =
				context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}
}
