package com.yapp.breake.core.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ScreenTimeService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private lateinit var notificationManager: NotificationManager
    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "ScreenTimeServiceChannel"
        private const val NOTIFICATION_ID = 1
        const val ACTION_START_SERVICE_WITH_DURATION =
            "com.example.myapplication.tracker.action.START_SERVICE_WITH_DURATION"
        const val ACTION_STOP_SERVICE = "com.example.myapplication.tracker.action.STOP_SERVICE"
        const val EXTRA_DURATION_SECONDS = "extra_duration_seconds"

        fun startServiceWithDuration(context: Context, durationSeconds: Int) {
            val startIntent = Intent(context, ScreenTimeService::class.java).apply {
                action = ACTION_START_SERVICE_WITH_DURATION
                putExtra(EXTRA_DURATION_SECONDS, durationSeconds)
            }
            context.startForegroundService(startIntent)
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ScreenTimeService::class.java)
            stopIntent.action = ACTION_STOP_SERVICE
            context.startService(stopIntent)
        }
    }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel()
        Timber.d("onCreate: 서비스가 생성되었습니다.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Timber.i("onStartCommand 수신. Action: ${intent?.action}, StartId: $startId")
        when (intent?.action) {
            ACTION_START_SERVICE_WITH_DURATION -> {
                val durationSeconds = intent.getIntExtra(EXTRA_DURATION_SECONDS, 0)
                Timber.i("Action: START_SERVICE_WITH_DURATION, 시간: ${durationSeconds}초")
                if (durationSeconds > 0) {
                    Timber.d("포그라운드 서비스를 시작할 준비 중입니다.")
                    val notification = createNotification("타이머 시작됨: ${durationSeconds}초")
                    try {
                        Timber.d("startForeground 호출 중, NOTIFICATION_ID: $NOTIFICATION_ID")
                        startForeground(NOTIFICATION_ID, notification)
                        Timber.i("startForeground 성공. 서비스가 포그라운드 상태입니다.")
                        startTimer(durationSeconds)
                    } catch (e: Exception) {
                        Timber.e(e, "startForeground 호출 오류: ${e.message}")
                        stopSelf()
                    }
                } else {
                    Timber.w("유효하지 않은 시간 ($durationSeconds 초). 서비스를 중지합니다.")
                    stopSelf()
                }
            }

            ACTION_STOP_SERVICE -> {
                Timber.i("Action: ACTION_STOP_SERVICE. 타이머와 서비스를 중지합니다.")
                stopTimerAndService()
            }

            null -> {
                Timber.w("onStartCommand가 null action을 수신했습니다. 시스템에 의해 서비스가 재시작될 때 발생할 수 있습니다.")
            }

            else -> {
                Timber.w("onStartCommand가 알 수 없는 action을 수신했습니다: ${intent.action}")
            }
        }
        return START_STICKY
    }

    private fun startTimer(durationSeconds: Int) {
        countDownTimer?.cancel()
        val durationMillis = TimeUnit.SECONDS.toMillis(durationSeconds.toLong())
        Timber.d("${durationMillis}ms 타이머를 시작합니다.")
        countDownTimer = object : CountDownTimer(durationMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutesLeft = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                val secondsLeft = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                val timeLeftFormatted = String.format("%02d:%02d", minutesLeft, secondsLeft)
                updateNotification("남은 시간: $timeLeftFormatted")

                serviceScope.launch {
                    // ...타이머 tick 이벤트 처리...
                }
            }

            override fun onFinish() {
                Timber.d("타이머 종료. 알림을 '시간 만료됨'으로 업데이트합니다.")
                updateNotification("시간 만료됨")
                serviceScope.launch {
                    // ...타이머 종료 이벤트 처리...
                }
            }
        }.start()
    }

    private fun stopTimerAndService() {
        Timber.d("stopTimerAndService가 호출되었습니다.")
        countDownTimer?.cancel()
        countDownTimer = null
        serviceScope.launch {
            // ...타이머 종료 이벤트 처리...
        }
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
        Timber.i("타이머가 종료/중지되어 서비스가 중지되었습니다.")
    }

    private fun updateNotification(contentText: String) {
        val notification = createNotification(contentText)
        Timber.d("알림 업데이트 (ID: $NOTIFICATION_ID), 내용: $contentText")
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "Screen Time Service Channel",
            NotificationManager.IMPORTANCE_LOW,
        ).apply {
            description = "스크린 타임 추적 서비스 알림 채널"
        }
        val manager = getSystemService(NotificationManager::class.java)
        if (manager != null) {
            manager.createNotificationChannel(serviceChannel)
            Timber.d("알림 채널 생성/업데이트됨: $NOTIFICATION_CHANNEL_ID")
        } else {
            Timber.e("NotificationManager가 null이므로 채널을 생성할 수 없습니다.")
        }
    }

    private fun createNotification(contentText: String): Notification {
        Timber.d("알림 생성 중, 내용: $contentText")
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("스크린 타임 오버레이")
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        Timber.i("onDestroy: 서비스가 소멸됩니다.")
        if (countDownTimer != null) {
            countDownTimer?.cancel()
            countDownTimer = null
        }
        serviceJob.cancel()
        super.onDestroy()
    }
}
