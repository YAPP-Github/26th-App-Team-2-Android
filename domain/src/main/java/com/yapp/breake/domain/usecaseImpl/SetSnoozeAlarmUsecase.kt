package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.usecase.SetSnoozeAlarmUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SetSnoozeAlarmUsecase @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
) : SetSnoozeAlarmUseCase {

	override suspend operator fun invoke(
		groupId: Long,
		appName: String,
	): Result<LocalDateTime> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_BLOCKING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(Constants.TEST_SNOOZE_TIME.toLong())

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			appName = appName,
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_USING,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Using,
				endTime = triggerTime,
			)
			appGroupRepository.insertSnooze(
				groupId = groupId,
			)
		}
	}
}
