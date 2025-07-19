package com.yapp.breake.domain.usecase

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import javax.inject.Inject

class SetAlarmUsecase @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
) {

	suspend operator fun invoke(
		groupId: Long,
		appGroupState: AppGroupState,
		second: Int = 0,
	): Result<Unit> {
		return when (appGroupState) {
			AppGroupState.NeedSetting -> return Result.failure(IllegalStateException("알람을 예약하지 않는 상태입니다."))
			AppGroupState.Using -> {
				scheduleAlarm(
					groupId = groupId,
					second = second,
					action = AlarmAction.ACTION_USING_FINISH,
					appGroupState = appGroupState,
				)
			}

			AppGroupState.Blocking -> {
				scheduleAlarm(
					groupId = groupId,
					second = Constants.TEST_BLOCKING_TIME,
					action = AlarmAction.ACTION_BLOCKING_FINISH,
					appGroupState = appGroupState,
				)
			}

			is AppGroupState.SnoozeBlocking -> {
				scheduleAlarm(
					groupId = groupId,
					second = Constants.TEST_SNOOZE_TIME,
					action = AlarmAction.ACTION_SNOOZE_FINISH,
					appGroupState = appGroupState,
				)
			}
		}
	}

	private suspend fun scheduleAlarm(
		groupId: Long,
		second: Int,
		action: AlarmAction,
		appGroupState: AppGroupState,
	): Result<Unit> {
		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			second = second,
			action = action,
		).onSuccess {
			appGroupRepository.setAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
			)
		}
	}
}
