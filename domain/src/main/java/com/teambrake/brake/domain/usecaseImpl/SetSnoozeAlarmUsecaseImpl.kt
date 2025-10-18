package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.repository.AlarmScheduler
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.etc.ConstTimeProvider
import com.teambrake.brake.domain.usecase.SetSnoozeAlarmUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SetSnoozeAlarmUsecaseImpl @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
	private val constTimeProvider: ConstTimeProvider,
) : SetSnoozeAlarmUseCase {

	override suspend operator fun invoke(
		groupId: Long,
		groupName: String,
	): Result<LocalDateTime> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_BLOCKING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(constTimeProvider.snoozeTime)

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = groupName,
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_USING,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Using,
				startTime = startTime,
				endTime = triggerTime,
			)
			appGroupRepository.insertSnooze(
				groupId = groupId,
			)
		}
	}
}
