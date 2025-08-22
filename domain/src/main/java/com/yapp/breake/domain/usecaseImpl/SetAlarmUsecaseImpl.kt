package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.common.AlarmAction
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.domain.repository.AlarmScheduler
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.etc.ConstTimeProvider
import com.yapp.breake.domain.usecase.SetAlarmUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SetAlarmUsecaseImpl @Inject constructor(
	private val alarmScheduler: AlarmScheduler,
	private val appGroupRepository: AppGroupRepository,
	private val constTimeProvider: ConstTimeProvider,
) : SetAlarmUseCase {

	override suspend operator fun invoke(
		groupId: Long,
		groupName: String,
		appGroupState: AppGroupState,
		second: Int,
		isUsingApp: Boolean,
	): Result<LocalDateTime> {
		val (action, time) = when (appGroupState) {
			AppGroupState.Using -> AlarmAction.ACTION_USING to constTimeProvider.getTime(second.toLong())
			AppGroupState.Blocking -> AlarmAction.ACTION_BLOCKING to constTimeProvider.blockingTime
			else -> {
				return Result.failure(IllegalStateException("알람을 예약하지 않는 상태입니다."))
			}
		}

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(time)

		return alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = groupName,
			triggerTime = triggerTime,
			action = action,
		).onSuccess {
			if (appGroupState == AppGroupState.Using) {
				appGroupRepository.updateGroupSessionInfo(
					groupId = groupId,
					goalMinutes = second,
					sessionStartTime = startTime,
				)
			}

			appGroupRepository.updateAppGroupState(
				groupId = groupId,
				appGroupState = if (isUsingApp) {
					AppGroupState.SnoozeBlocking
				} else {
					appGroupState
				},
				startTime = startTime,
				endTime = triggerTime,
			)
		}
	}
}
