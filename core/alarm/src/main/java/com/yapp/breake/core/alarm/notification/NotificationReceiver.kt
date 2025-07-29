package com.yapp.breake.core.alarm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yapp.breake.core.alarm.scheduler.AlarmSchedulerImpl.Companion.EXTRA_APP_NAME_ID
import com.yapp.breake.core.alarm.scheduler.AlarmSchedulerImpl.Companion.EXTRA_GROUP_ID
import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.core.util.AppLaunchUtil
import com.yapp.breake.core.util.OverlayLauncher
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.ResetAppGroupUsecase
import com.yapp.breake.domain.usecase.SetAlarmUseCase
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
			val groupId = intent.getLongExtra(EXTRA_GROUP_ID, 0)
			val appName = intent.getStringExtra(EXTRA_APP_NAME_ID)
			val appGroup = appGroupRepository.getAppGroupById(groupId)
			val intentAction = intent.action

			if (appGroup != null && intentAction != null) {
				when (AlarmAction.valueOf(intentAction)) {
					AlarmAction.ACTION_USING -> startBlocking(context, appGroup, appName)
					AlarmAction.ACTION_BLOCKING -> stopBlocking(context, appGroup, appName)
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
	private suspend fun startBlocking(context: Context, appGroup: AppGroup, appName: String?) {
		Timber.i("ID: ${appGroup.id} 차단이 시작되었습니다")

		val isUserUsingApp = AppLaunchUtil.isAppLaunching(context, appGroup)
		if (isUserUsingApp) {
			OverlayLauncher.startOverlay(
				context = context,
				appGroup = appGroup,
				appName = appName,
				appGroupState = AppGroupState.SnoozeBlocking,
				snoozesCount = appGroup.snoozesCount,
			)
		}

		setAlarmUsecase(
			groupId = appGroup.id,
			appGroupState = AppGroupState.Blocking,
			appName = appName ?: "Unknown App",
		)
	}

	private suspend fun stopBlocking(context: Context, appGroup: AppGroup, appName: String?) {
		Timber.i("ID: ${appGroup.id} 차단이 해제되었습니다")
		resetAppGroupUsecase(groupId = appGroup.id)

		val isUserUsingApp = AppLaunchUtil.isAppLaunching(context, appGroup)
		if (isUserUsingApp) {
			OverlayLauncher.startOverlay(
				context = context,
				appGroup = appGroup,
				appName = appName,
				appGroupState = AppGroupState.NeedSetting,
			)
		}
	}
}
