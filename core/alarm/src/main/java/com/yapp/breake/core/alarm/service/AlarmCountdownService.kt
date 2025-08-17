package com.yapp.breake.core.alarm.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
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
	private var remainingNotificationShown = false
	private val remainingTimeAlert = 60_000L

	override fun onCreate() {
		super.onCreate()
		Timber.d("AlarmCountdownService onCreate")
		notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		createNotificationChannel()
		showInitialNotification()
	}

	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		Timber.d("AlarmCountdownService onStartCommand")

		if (intent?.action == ACTION_CANCEL_ALARM) {
			Timber.d("Cancel alarm action received")
			stopSelf()
			return START_NOT_STICKY
		}

		val groupName = intent?.getStringExtra(EXTRA_GROUP_NAME) ?: ""
		val triggerTimeString = intent?.getStringExtra(EXTRA_TRIGGER_TIME) ?: ""

		if (triggerTimeString.isEmpty()) {
			Timber.w("Invalid parameters, stopping service")
			stopSelf()
			return START_NOT_STICKY
		}

		val triggerTime = LocalDateTime.parse(
			triggerTimeString,
			DateTimeFormatter.ISO_LOCAL_DATE_TIME,
		)

		startCountdown(groupName, triggerTime)

		return START_STICKY
	}

	override fun onBind(intent: Intent?): IBinder? = null

	override fun onDestroy() {
		super.onDestroy()
		serviceJob?.cancel()
	}

	private fun startCountdown(groupName: String, triggerTime: LocalDateTime) {
		serviceJob?.cancel()
		remainingNotificationShown = false

		val now = LocalDateTime.now()
		initialRemainingTime =
			triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
			now.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

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

				if (!remainingNotificationShown && remainingTime <= remainingTimeAlert) {
					showWarningNotification()
					remainingNotificationShown = true
				}

				updateCustomNotification(groupName, remainingTime, triggerTime)
				delay(1.seconds)
			}
		}
	}

	private fun updateCustomNotification(
		groupName: String,
		remainingTimeMillis: Long,
		triggerTime: LocalDateTime,
	) {
		val targetTime =
			triggerTime.format(
				DateTimeFormatter.ofPattern(getString(R.string.time_format_hour_minute), Locale.getDefault()),
			)

		val totalMinutes = (remainingTimeMillis / (1000 * 60)).toInt()
		val collapsedTitle = getString(R.string.alarm_using_format, groupName, totalMinutes)
		val collapsedContent = getString(R.string.alarm_available_until_format, targetTime)

		val progress = if (initialRemainingTime > 0) {
			val elapsedTime = initialRemainingTime - remainingTimeMillis
			((elapsedTime * 100) / initialRemainingTime).coerceIn(0, 100).toInt()
		} else {
			0
		}

		val largeIcon = try {
			(
				ResourcesCompat.getDrawable(
					this.resources,
					R.drawable.ic_large_alarm,
					null,
				) as VectorDrawable
				).toBitmap()
		} catch (e: Exception) {
			Timber.w("Failed to decode large icon: ${e.message}")
			null
		}

		val contentIntent = createMainActivityPendingIntent()

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_alarm)
			.setColor(ContextCompat.getColor(this, android.R.color.transparent))
			.setContentTitle(collapsedTitle)
			.setContentText(collapsedContent)
			.setContentIntent(contentIntent)
			.setProgress(100, progress, false)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setCategory(NotificationCompat.CATEGORY_ALARM)
			.setOngoing(true)
			.setAutoCancel(false)
			.setSilent(true)
			.setColorized(false)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setShowWhen(false)
			.setStyle(
				NotificationCompat.BigPictureStyle()
					.setBigContentTitle(collapsedTitle)
					.setSummaryText(collapsedContent)
					.bigLargeIcon(largeIcon),
			)
			.build()

		notification.flags = notification.flags or android.app.Notification.FLAG_NO_CLEAR

		try {
			startForeground(NOTIFICATION_ID, notification)
		} catch (e: Exception) {
			Timber.e("Failed to update custom notification: $e")
		}
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			CHANNEL_ID,
			getString(R.string.alarm_countdown_channel_name),
			NotificationManager.IMPORTANCE_DEFAULT,
		).apply {
			description = getString(R.string.alarm_countdown_channel_description)
			enableLights(false)
			enableVibration(false)
			setShowBadge(true)
			lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
			setSound(null, null)
		}

		val warningChannel = NotificationChannel(
			WARNING_CHANNEL_ID,
			getString(R.string.alarm_warning_channel_name),
			NotificationManager.IMPORTANCE_HIGH,
		).apply {
			description = getString(R.string.alarm_warning_channel_description)
			enableLights(true)
			enableVibration(true)
			setShowBadge(true)
			lockscreenVisibility = android.app.Notification.VISIBILITY_PUBLIC
		}

		notificationManager?.createNotificationChannel(channel)
		notificationManager?.createNotificationChannel(warningChannel)
		Timber.d("Notification channels created")
	}

	private fun showInitialNotification() {
		val contentIntent = createMainActivityPendingIntent()

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_alarm)
			.setContentTitle(getString(R.string.alarm_preparing_title))
			.setContentText(getString(R.string.alarm_preparing_content))
			.setContentIntent(contentIntent)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setCategory(NotificationCompat.CATEGORY_ALARM)
			.setOngoing(true)
			.setAutoCancel(false)
			.setSilent(true)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setShowWhen(false)
			.build()

		try {
			startForeground(NOTIFICATION_ID, notification)
			Timber.d("Initial notification shown")
		} catch (e: Exception) {
			Timber.e("Failed to show initial notification: $e")
		}
	}

	private fun showWarningNotification() {
		val contentIntent = createMainActivityPendingIntent()

		val notification = NotificationCompat.Builder(this, WARNING_CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_alarm)
			.setContentTitle(getString(R.string.alarm_warning_title))
			.setContentText(getString(R.string.alarm_warning_content))
			.setContentIntent(contentIntent)
			.setPriority(NotificationCompat.PRIORITY_HIGH)
			.setCategory(NotificationCompat.CATEGORY_ALARM)
			.setAutoCancel(true)
			.setDefaults(NotificationCompat.DEFAULT_ALL)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setShowWhen(true)
			.build()

		try {
			notificationManager?.notify(WARNING_NOTIFICATION_ID, notification)
			Timber.d("Warning notification shown")
		} catch (e: Exception) {
			Timber.e("Warning notification: $e")
		}
	}

	private fun createMainActivityPendingIntent(): PendingIntent? {
		return try {
			val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
			launchIntent?.let { intent ->
				intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
				PendingIntent.getActivity(
					this,
					0,
					intent,
					PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
				)
			}
		} catch (e: Exception) {
			Timber.e("Failed to create main activity pending intent: $e")
			null
		}
	}

	companion object {
		const val CHANNEL_ID = "alarm_countdown_channel"
		const val WARNING_CHANNEL_ID = "alarm_warning_channel"
		const val NOTIFICATION_ID = 1001
		const val WARNING_NOTIFICATION_ID = 1002
		const val EXTRA_GROUP_NAME = "EXTRA_GROUP_NAME"
		const val EXTRA_TRIGGER_TIME = "EXTRA_TRIGGER_TIME"
		const val ACTION_CANCEL_ALARM = "ACTION_CANCEL_ALARM"

		fun start(context: Context, groupName: String, triggerTime: LocalDateTime) {
			try {
				Timber.d("Starting AlarmCountdownService")
				val intent = Intent(context, AlarmCountdownService::class.java).apply {
					putExtra(EXTRA_GROUP_NAME, groupName)
					putExtra(
						EXTRA_TRIGGER_TIME,
						triggerTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
					)
				}
				context.startForegroundService(intent)
				Timber.d("AlarmCountdownService started successfully")
			} catch (e: Exception) {
				Timber.e("포그라운드 서비스 시작 실패: $e")
			}
		}

		fun stop(context: Context) {
			try {
				val intent = Intent(context, AlarmCountdownService::class.java)
				context.stopService(intent)
			} catch (e: Exception) {
				Timber.e("포그라운드 서비스 중지 실패: $e")
			}
		}
	}
}
