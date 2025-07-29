package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.ConstTimeProvider
import com.yapp.breake.domain.usecase.SetBlockingAlarmUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SetBlockingAlarmUseCaseImpl @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
	private val constTimeProvider: ConstTimeProvider,
) : SetBlockingAlarmUseCase {

	override suspend operator fun invoke(
		groupId: Long,
		appName: String,
	): Result<LocalDateTime> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_USING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(constTimeProvider.blockingTime)

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			appName = appName,
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_BLOCKING,
		).onSuccess {
			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = AppGroupState.Blocking,
				endTime = triggerTime,
			)
		}
	}
}
