package com.yapp.breake.core.alarm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.yapp.breake.core.alarm.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class AlarmCountdownService : Service() {

	private var serviceJob: Job? = null
	private var notificationManager: NotificationManager? = null
	private var initialRemainingTime: Long = 0L

	override fun onCreate() {
		super.onCreate()
		Timber.d("AlarmCountdownService onCreate")
		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		createNotificationChannel()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Timber.d("AlarmCountdownService onStartCommand")

		if (intent?.action == ACTION_CANCEL_ALARM) {
			Timber.d("Cancel alarm action received")
			stopSelf()
			return START_NOT_STICKY
		}

		val groupId = intent?.getLongExtra(EXTRA_GROUP_ID, -1L) ?: -1L
		val groupName = intent?.getStringExtra(EXTRA_GROUP_NAME) ?: ""
		val triggerTimeString = intent?.getStringExtra(EXTRA_TRIGGER_TIME) ?: ""

		if (groupId == -1L || triggerTimeString.isEmpty()) {
			Timber.w("Invalid parameters, stopping service")
			stopSelf()
			return START_NOT_STICKY
		}

		val triggerTime = LocalDateTime.parse(
			triggerTimeString,
			DateTimeFormatter.ISO_LOCAL_DATE_TIME,
		)

		startCountdown(groupId, groupName, triggerTime)

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? = null

	override fun onDestroy() {
		super.onDestroy()
		serviceJob?.cancel()
	}

	private fun startCountdown(groupId: Long, groupName: String, triggerTime: LocalDateTime) {
		serviceJob?.cancel()

		val now = LocalDateTime.now()
		initialRemainingTime =
			triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
			now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

		Timber.d("Initial remaining time: ${initialRemainingTime}ms")

		serviceJob = CoroutineScope(Dispatchers.Main).launch {
			while (isActive) {
				val currentTime = LocalDateTime.now()
				val remainingTime =
					triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
						currentTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

				if (remainingTime <= 0) {
					Timber.d("Countdown finished, stopping service")
					stopSelf()
					break
				}

				updateCustomNotification(groupId, groupName, remainingTime, triggerTime)
				delay(1.seconds)
			}
		}
	}

	private fun updateCustomNotification(
		groupId: Long,
		groupName: String,
		remainingTimeMillis: Long,
		triggerTime: LocalDateTime,
	) {
		val hours = remainingTimeMillis / (1000 * 60 * 60)
		val minutes = (remainingTimeMillis % (1000 * 60 * 60)) / (1000 * 60)
		val seconds = (remainingTimeMillis % (1000 * 60)) / 1000

		val timeText = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds)
		val targetTime = triggerTime.format(DateTimeFormatter.ofPattern("HH시 mm분", Locale.getDefault()))

		val totalMinutes = (remainingTimeMillis / (1000 * 60)).toInt()
		val collapsedTitle = "$groupName 사용중 • ${totalMinutes}분 남음"
		val collapsedContent = "${targetTime}까지 사용 가능"

		Timber.d("Updating notification - Remaining: $timeText, Target: $targetTime")

		val cancelIntent = Intent(this, AlarmCountdownService::class.java).apply {
			action = ACTION_CANCEL_ALARM
			putExtra(EXTRA_GROUP_ID, groupId)
		}
		val cancelPendingIntent = PendingIntent.getService(
			this,
			groupId.toInt(),
			cancelIntent,
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
		)

		val progress = if (initialRemainingTime > 0) {
			val elapsedTime = initialRemainingTime - remainingTimeMillis
			((elapsedTime * 100) / initialRemainingTime).coerceIn(0, 100).toInt()
		} else {
			0
		}

		val expandedViews = RemoteViews(packageName, R.layout.notification_countdown_expanded)
		expandedViews.setTextViewText(R.id.tv_app_name_expanded, groupName)
		expandedViews.setTextViewText(R.id.tv_description_expanded, "알람 시간: $targetTime")
		expandedViews.setTextViewText(R.id.tv_countdown_expanded, timeText)
		expandedViews.setTextViewText(R.id.tv_remaining_info, "남은 시간")
		expandedViews.setProgressBar(R.id.progress_countdown_expanded, 100, progress, false)
		expandedViews.setOnClickPendingIntent(R.id.btn_cancel_expanded, cancelPendingIntent)

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_alarm_notification)
			.setContentTitle(collapsedTitle)
			.setContentText(collapsedContent)
			.setCustomBigContentView(expandedViews)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setCategory(NotificationCompat.CATEGORY_ALARM)
			.setOngoing(true)
			.setAutoCancel(false)
			.setSilent(true)
			.setColorized(true)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setShowWhen(false)
			.addAction(
				R.drawable.ic_close,
				"취소",
				cancelPendingIntent,
			)
			.build()

		notification.flags = notification.flags or android.app.Notification.FLAG_NO_CLEAR

		try {
			startForeground(NOTIFICATION_ID, notification)
			Timber.d("Notification updated - Collapsed: $collapsedTitle, Expanded: $timeText")
		} catch (e: Exception) {
			Timber.e("Failed to update custom notification: $e")
		}
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			CHANNEL_ID,
			"알람 카운트다운",
			NotificationManager.IMPORTANCE_DEFAULT,
		).apply {
			description = "알람까지의 남은 시간을 실시간으로 표시합니다"
			enableLights(false)
			enableVibration(false)
			setShowBadge(true)
			lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
			setSound(null, null)
		}
		notificationManager?.createNotificationChannel(channel)
		Timber.d("Notification channel created")
	}

	companion object {
		const val CHANNEL_ID = "alarm_countdown_channel"
		const val NOTIFICATION_ID = 1001
		const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
		const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
		const val EXTRA_TRIGGER_TIME = "EXTRA_TRIGGER_TIME"
		const val ACTION_CANCEL_ALARM = "ACTION_CANCEL_ALARM"

		fun start(context: Context, groupId: Long, groupName: String, triggerTime: LocalDateTime) {
			val intent = Intent(context, AlarmCountdownService::class.java).apply {
				putExtra(EXTRA_GROUP_ID, groupId)
				putExtra(EXTRA_GROUP_NAME, groupName)
				putExtra(
					EXTRA_TRIGGER_TIME,
					triggerTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				)
			}
			context.startForegroundService(intent)
		}

		fun stop(context: Context) {
			val intent = Intent(context, AlarmCountdownService::class.java)
			context.stopService(intent)
		}
	}
}
