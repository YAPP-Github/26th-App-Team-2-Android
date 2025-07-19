package com.yapp.breake.core.alarm.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yapp.breake.core.alarm.notification.NotificationReceiver
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.utils.AlarmAction
import com.yapp.breake.domain.repository.AppGroupRepository
import timber.log.Timber
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(
	private val alarmManager: AlarmManager,
	private val appGroupRepository: AppGroupRepository,
	private val context: Context,
) : AlarmScheduler {

	override suspend fun scheduleAlarm(
		groupId: Long,
		appGroupState: AppGroupState,
		minute: Int,
	): Result<Unit> {
		return when (appGroupState) {
			AppGroupState.NeedSetting -> return Result.failure(IllegalStateException("알람을 예약하지 않는 상태입니다."))
			AppGroupState.Using -> {
				scheduleAlarmWithAction(
					groupId = groupId,
					minute = minute,
					action = AlarmAction.ACTION_USING_FINISH,
					appGroupState = appGroupState,
				)
			}
			AppGroupState.Blocking -> {
				scheduleAlarmWithAction(
					groupId = groupId,
					minute = Constants.BLOCKING_TIME,
					action = AlarmAction.ACTION_BLOCKING_FINISH,
					appGroupState = appGroupState,
				)
			}
			is AppGroupState.SnoozeBlocking -> {
				scheduleAlarmWithAction(
					groupId = groupId,
					minute = Constants.SNOOZE_TIME,
					action = AlarmAction.ACTION_SNOOZE_FINISH,
					appGroupState = appGroupState,
				)
			}
		}
	}

	override suspend fun cancelAlarm(groupId: Long) {
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = AlarmAction.ACTION_USING_FINISH.name
		}

		val pendingIntentFlags = PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
		val pendingIntent = PendingIntent.getBroadcast(
			context,
			groupId.toInt(),
			intent,
			pendingIntentFlags,
		)

		pendingIntent?.let {
			alarmManager.cancel(it)
			it.cancel()
		}
	}

	private suspend fun scheduleAlarmWithAction(
		groupId: Long,
		minute: Int,
		action: AlarmAction,
		appGroupState: AppGroupState,
	): Result<Unit> {
		if (!canScheduleExactAlarms()) {
			val errorMessage = "정확한 알람 권한이 없습니다. ID: $groupId 에 대한 정확한 알람을 예약할 수 없습니다."
			Timber.w(errorMessage)
			return Result.failure(SecurityException("정확한 알람 권한이 없습니다."))
		}

		val intent = getPendingIntent(groupId, action.name)

		return try {
			scheduleAlarm(minute, intent)
			appGroupRepository.setAppGroupState(groupId = groupId, appGroupState = appGroupState)
			Result.success(Unit)
		} catch (se: SecurityException) {
			Timber.e("SecurityException: ID: $groupId 에 대한 정확한 알람을 예약할 수 없습니다. $se")
			Result.failure(se)
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
		intentAction: String,
	): PendingIntent {
		val intent = Intent(context, NotificationReceiver::class.java).apply {
			action = intentAction
			putExtra(EXTRA_GROUP_ID, groupId)
		}

		val pendingIntentFlags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
		return PendingIntent.getBroadcast(
			context,
			groupId.toInt(),
			intent,
			pendingIntentFlags,
		)
	}

	private fun scheduleAlarm(
		minute: Int,
		pendingIntent: PendingIntent,
	) {
		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusMinutes(minute.toLong())
		val triggerAtMillis =
			triggerTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
		alarmManager.setExactAndAllowWhileIdle(
			AlarmManager.RTC_WAKEUP,
			triggerAtMillis,
			pendingIntent,
		)
	}
	companion object {
		const val EXTRA_GROUP_ID = "EXTRA_GROUP_ID"
	}
}
