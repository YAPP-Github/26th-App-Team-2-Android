package com.teambrake.brake.core.alarm.service

import android.app.ActivityManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.VectorDrawable
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.teambrake.brake.core.alarm.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

class AlarmCountdownService : Service() {

	private var serviceJob: Job? = null
	private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
	private var notificationManager: NotificationManager? = null
	private var initialRemainingTime: Long = 0L
	private var remainingNotificationShown = false
	private val remainingTimeAlert = 60_000L
	private var isForegroundStarted = false

	private val largeIconBitmap: Bitmap? by lazy {
		try {
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
	}

	override fun onCreate() {
		super.onCreate()
		Timber.d("AlarmCountdownService onCreate")
		notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
		createNotificationChannel()
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
		serviceScope.cancel()
		Timber.d("AlarmCountdownService destroyed")
	}

	private fun startCountdown(groupName: String, triggerTime: LocalDateTime) {
		serviceJob?.cancel()
		remainingNotificationShown = false
		isForegroundStarted = false

		val triggerTimeMillis =
			triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
		initialRemainingTime = triggerTimeMillis - System.currentTimeMillis()

		updateCustomNotification(groupName, initialRemainingTime, triggerTime)

		serviceJob = serviceScope.launch {
			while (isActive) {
				val remainingTime = triggerTimeMillis - System.currentTimeMillis()

				if (remainingTime <= 0) {
					stopSelf()
					break
				}

				if (!remainingNotificationShown && remainingTime <= remainingTimeAlert) {
					showWarningNotification()
					remainingNotificationShown = true
				}

				updateCustomNotification(groupName, remainingTime, triggerTime)

				// delay 시간 계산: 1분 이하면 1초, 1분 이상이면 다음 분까지의 시간
				val now = System.currentTimeMillis()
				val nextMinuteBoundary = ((now / 60_000) + 1) * 60_000
				val millisUntilNextMinute = (nextMinuteBoundary - now).coerceAtLeast(0L)

				val delayTime = if (remainingTime <= 60_000) {
					1.seconds
				} else {
					millisUntilNextMinute.milliseconds + 50.milliseconds
				}
				delay(delayTime)
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
				DateTimeFormatter.ofPattern(
					getString(R.string.time_format_hour_minute),
					Locale.getDefault(),
				),
			)

		// 1분 이하일 때는 초 단위로 표시
		val displayText = if (remainingTimeMillis <= 60_060) {
			val seconds = (remainingTimeMillis / 1000).toInt()
			try {
				getString(R.string.alarm_using_format_seconds, groupName, seconds)
			} catch (_: Exception) {
				"$groupName 사용 중 (${seconds}초 남음)"
			}
		} else {
			// 1분 이상일 때는 분 단위로 표시
			val totalMinutes = (remainingTimeMillis / 60_000).toInt()
			getString(R.string.alarm_using_format, groupName, totalMinutes)
		}

		val collapsedContent = getString(R.string.alarm_available_until_format, targetTime)

		val progress = if (initialRemainingTime > 0) {
			val elapsedTime = initialRemainingTime - remainingTimeMillis
			((elapsedTime * 100) / initialRemainingTime).coerceIn(0, 100).toInt()
		} else {
			0
		}

		val contentIntent = createMainActivityPendingIntent()

		val notification = NotificationCompat.Builder(this, CHANNEL_ID)
			.setSmallIcon(R.drawable.ic_alarm)
			.setColor(ContextCompat.getColor(this, android.R.color.transparent))
			.setContentTitle(displayText)
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
					.setBigContentTitle(displayText)
					.setSummaryText(collapsedContent)
					.bigLargeIcon(largeIconBitmap),
			)
			.build()

		notification.flags = Notification.FLAG_FOREGROUND_SERVICE

		try {
			if (!isForegroundStarted) {
				// 최초 1회만 startForeground 호출
				startForeground(NOTIFICATION_ID, notification)
				isForegroundStarted = true
				Timber.d("Foreground service started")
			}

			// 기존 호출된 notification 을 ID 를 통해 추적하여 notify 로 업데이트
			notificationManager?.notify(NOTIFICATION_ID, notification)
			if (remainingTimeMillis <= 60_000) {
				Timber.d("Notification updated: ${remainingTimeMillis / 1000} seconds remaining")
			} else {
				Timber.d("Notification updated: ${remainingTimeMillis / 60_000} minutes remaining")
			}
		} catch (e: Exception) {
			Timber.e("Failed to update notification: $e")
		}
	}

	private fun createNotificationChannel() {
		val channel = NotificationChannel(
			CHANNEL_ID,
			getString(R.string.alarm_countdown_channel_name),
			NotificationManager.IMPORTANCE_HIGH,
		).apply {
			description = getString(R.string.alarm_countdown_channel_description)
			enableLights(false)
			enableVibration(false)
			setShowBadge(true)
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
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
			lockscreenVisibility = Notification.VISIBILITY_PUBLIC
		}

		notificationManager?.createNotificationChannel(channel)
		notificationManager?.createNotificationChannel(warningChannel)
		Timber.d("Notification channels created")
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

		fun startForegroundNotification(
			context: Context,
			groupName: String,
			triggerTime: LocalDateTime,
		) {
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

		fun isRunning(context: Context): Boolean {
			val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
			return manager.let { activityManager ->
				@Suppress("DEPRECATION")
				activityManager.getRunningServices(Int.MAX_VALUE).any { serviceInfo ->
					serviceInfo.service.className == AlarmCountdownService::class.java.name
				}
			}
		}
	}
}
