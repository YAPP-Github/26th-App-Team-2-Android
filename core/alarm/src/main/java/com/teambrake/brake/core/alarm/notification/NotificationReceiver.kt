package com.teambrake.brake.core.alarm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.datastore.core.DataStore
import com.amplitude.android.Amplitude
import com.amplitude.core.events.Identify
import com.teambrake.brake.core.alarm.scheduler.AlarmSchedulerImpl
import com.teambrake.brake.core.amplitude.AmplitudeEventHelper
import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.datastore.model.DatastoreFeedback
import com.teambrake.brake.core.model.accessibility.IntentConfig
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.usecase.ResetAppGroupUsecase
import com.teambrake.brake.domain.usecase.SetAlarmUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

	@Inject
	lateinit var appGroupRepository: AppGroupRepository

	@Inject
	lateinit var setAlarmUsecase: SetAlarmUseCase

	@Inject
	lateinit var resetAppGroupUsecase: ResetAppGroupUsecase

	@Inject
	lateinit var amplitude: Amplitude

	@Inject
	lateinit var feedbackDataStore: DataStore<DatastoreFeedback>

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	override fun onReceive(context: Context, intent: Intent) {
		val pendingResult = goAsync()

		serviceScope.launch {
			try {
				val groupId = intent.getLongExtra(AlarmSchedulerImpl.Companion.EXTRA_GROUP_ID, 0)
				val appGroup = appGroupRepository.getAppGroupById(groupId)
				val intentAction = intent.action

				if (appGroup != null) {
					when (AlarmAction.Companion.fromString(intentAction)) {
						AlarmAction.ACTION_USING -> startBlocking(context, appGroup)
						AlarmAction.ACTION_BLOCKING -> stopBlocking(context, appGroup)
					}
				}
			} finally {
				pendingResult.finish()
			}
		}
	}

	/**
	 *	차단 프로세스는 3가지 작업으로 진행
	 *  1. 상태 변경 - 사용 중이면 SNOOZE_BLOCKING, 사용 중이 아니면 BLOCKING 상태로 변경
	 *  2. 오버레이 시작 - 사용 중이면 오버레이 시작
	 *  3. 알람 스케줄러 시작 - 차단이 완료되면 알람 스케줄러를 시작
	 * */
	private suspend fun startBlocking(context: Context, appGroup: AppGroup) {
		val broadcastIntent = Intent().apply {
			action = IntentConfig.RECEIVER_IDENTITY
			setPackage(context.packageName)
			putExtra(IntentConfig.EXTRA_GROUP_ID, appGroup.id)
			putExtra(IntentConfig.EXTRA_GROUP_STATE, AppGroupState.SnoozeBlocking)
			putExtra(IntentConfig.EXTRA_SNOOZES_COUNT, appGroup.snoozesCount)
		}
		context.sendBroadcast(broadcastIntent)

		setAlarmUsecase(
			groupId = appGroup.id,
			groupName = appGroup.name,
			appGroupState = AppGroupState.Blocking,
		)
	}

	private suspend fun stopBlocking(context: Context, appGroup: AppGroup) {
		resetAppGroupUsecase(appGroup)

		withContext(Dispatchers.IO) {
			val startTime = appGroup.startTime ?: java.time.LocalDateTime.now()
			val plannedDuration = appGroup.goalMinutes ?: 0
			val elapsedDurationInSeconds = java.time.Duration.between(
				startTime,
				java.time.LocalDateTime.now(),
			).toSeconds().toInt()
			val snoozeCount = appGroup.snoozesCount

			/**
			 * 15. end_brake_session 이벤트 전송
			 * 세션이 계획된 시간보다 일찍 종료될 때 전송
			 */
			val event = AmplitudeEventHelper.createEndBrakeSessionEvent(
				plannedDuration = plannedDuration,
				elapsedDuration = elapsedDurationInSeconds / 60,
				snoozeCount = snoozeCount,
				isEarlyExit = elapsedDurationInSeconds / 60 < plannedDuration,
				groupId = appGroup.id.toString(),
				groupName = appGroup.name,
				groupAppCount = appGroup.apps.size,
			)
			amplitude.track(event.getEventName(), event.toEventProperties())

			/**
			 * 19. set_last_brake_session_date 유저 속성 설정
			 * 마지막 차단 세션 날짜를 설정하는 유저 속성
			 */
			val currentDate = java.time.LocalDate.now().toString() // YYYY-MM-DD 형식
			val userProperty = AmplitudeEventHelper.setLastBrakeSessionDate(date = currentDate)
			amplitude.identify(
				Identify().apply {
					userProperty.toUserProperties().forEach { (key, value) ->
						set(key, value)
					}
				},
			)

			feedbackDataStore.updateData { current ->
				current.copy(sessionCount = current.sessionCount + 1)
			}
		}

		val broadcastIntent = Intent().apply {
			action = IntentConfig.RECEIVER_IDENTITY
			putExtra(IntentConfig.EXTRA_GROUP_ID, appGroup.id)
			putExtra(IntentConfig.EXTRA_GROUP_STATE, AppGroupState.NeedSetting)
			putExtra(IntentConfig.EXTRA_SNOOZES_COUNT, 0)
		}
		context.sendBroadcast(broadcastIntent)
	}
}
