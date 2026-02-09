package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.SetBlockingAlarmUseCaseError
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
	): BrakeResult<LocalDateTime, SetBlockingAlarmUseCaseError> {

		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_USING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(constTimeProvider.blockingTime)

		val scheduleResult = alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = "",
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_BLOCKING,
		)

		return when {
			scheduleResult.isSuccess -> {
				val updateResult = appGroupRepository.updateAppGroupState(
					groupId = groupId,
					appGroupState = AppGroupState.Blocking,
					startTime = startTime,
					endTime = triggerTime,
				)

				when {
					updateResult.isSuccess -> {
						BrakeResult.Success(triggerTime)
					}

					updateResult.isFailure -> {
						val exception = updateResult.exceptionOrNull()
						BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 그룹 상태 업데이트 실패")))
					}

					else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
				}
			}

			scheduleResult.isFailure -> {
				val exception = scheduleResult.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Exception("알람 스케줄링 실패")))
			}

			else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
		}
	}
}
