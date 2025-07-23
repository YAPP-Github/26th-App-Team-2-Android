package com.yapp.breake.domain.usecase

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import java.time.LocalDateTime
import javax.inject.Inject

class SetSnoozeAlarmUsecase @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
) {

	suspend operator fun invoke(
		groupId: Long,
	): Result<LocalDateTime> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_BLOCKING,
		)

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			second = Constants.TEST_SNOOZE_TIME,
			action = AlarmAction.ACTION_USING,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Using,
			)
			appGroupRepository.insertSnooze(
				groupId = groupId,
			)
		}
	}
}
