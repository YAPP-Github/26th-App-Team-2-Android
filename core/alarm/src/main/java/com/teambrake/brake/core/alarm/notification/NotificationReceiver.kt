package com.teambrake.brake.core.alarm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.teambrake.brake.core.alarm.scheduler.AlarmSchedulerImpl
import com.teambrake.brake.core.common.AlarmAction
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
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

	@Inject
	lateinit var appGroupRepository: AppGroupRepository

	@Inject
	lateinit var setAlarmUsecase: SetAlarmUseCase

	@Inject
	lateinit var resetAppGroupUsecase: ResetAppGroupUsecase

	private val serviceJob = SupervisorJob()
	private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

	override fun onReceive(context: Context, intent: Intent) {
		serviceScope.launch {
			val groupId = intent.getLongExtra(AlarmSchedulerImpl.Companion.EXTRA_GROUP_ID, 0)
			val appGroup = appGroupRepository.getAppGroupById(groupId)
			val intentAction = intent.action

			if (appGroup != null) {
				when (AlarmAction.Companion.fromString(intentAction)) {
					AlarmAction.ACTION_USING -> startBlocking(context, appGroup)
					AlarmAction.ACTION_BLOCKING -> stopBlocking(context, appGroup)
				}
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
		Timber.i("ID: ${appGroup.id} 차단이 시작되었습니다")

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
		Timber.i("ID: ${appGroup.id} 차단이 해제되었습니다")
		resetAppGroupUsecase(appGroup)

		val broadcastIntent = Intent().apply {
			action = IntentConfig.RECEIVER_IDENTITY
			putExtra(IntentConfig.EXTRA_GROUP_ID, appGroup.id)
			putExtra(IntentConfig.EXTRA_GROUP_STATE, AppGroupState.NeedSetting)
			putExtra(IntentConfig.EXTRA_SNOOZES_COUNT, 0)
		}
		context.sendBroadcast(broadcastIntent)
	}
}
