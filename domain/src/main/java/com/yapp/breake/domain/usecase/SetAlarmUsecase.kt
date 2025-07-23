package com.yapp.breake.domain.usecase

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SetAlarmUsecase @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
) {

	suspend operator fun invoke(
		groupId: Long,
		appGroupState: AppGroupState,
		second: Int = 0,
	): Result<LocalDateTime> {

		val (action, time) = when (appGroupState) {
			AppGroupState.Using -> AlarmAction.ACTION_USING to second
			AppGroupState.Blocking -> AlarmAction.ACTION_BLOCKING to Constants.TEST_BLOCKING_TIME
			else -> {
				return Result.failure(IllegalStateException("알람을 예약하지 않는 상태입니다."))
			}
		}

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			second = time,
			action = action,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
			)
		}
	}
}
