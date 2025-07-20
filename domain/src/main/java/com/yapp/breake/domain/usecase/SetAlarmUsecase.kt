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

		val (action, time) = when (appGroupState) {
			AppGroupState.NeedSetting -> return Result.failure(IllegalStateException("알람을 예약하지 않는 상태입니다."))
			AppGroupState.Using -> AlarmAction.ACTION_USING to second
			AppGroupState.Blocking -> AlarmAction.ACTION_BLOCKING to Constants.TEST_BLOCKING_TIME
			is AppGroupState.SnoozeBlocking -> AlarmAction.ACTION_SNOOZE to Constants.TEST_SNOOZE_TIME
		}

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			second = time,
			action = action,
		).onSuccess {
			appGroupRepository.setAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
			)
		}
	}
}
