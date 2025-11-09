package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.repository.AlarmScheduler
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.etc.ConstTimeProvider
import com.teambrake.brake.domain.usecase.SetBlockingAlarmUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SetBlockingAlarmUseCaseImpl @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
	private val constTimeProvider: ConstTimeProvider,
) : SetBlockingAlarmUseCase {

	override suspend operator fun invoke(
		groupId: Long,
	): Result<LocalDateTime> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_USING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(constTimeProvider.blockingTime)

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = "",
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_BLOCKING,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Blocking,
				startTime = startTime,
				endTime = triggerTime,
			)
		}
	}
}
