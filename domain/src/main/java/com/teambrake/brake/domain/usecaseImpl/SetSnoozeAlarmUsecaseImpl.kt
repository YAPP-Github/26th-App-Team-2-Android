package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.SetSnoozeAlarmUseCaseError
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
	): BrakeResult<LocalDateTime, SetSnoozeAlarmUseCaseError> {
		alarmScheduler.cancelAlarm(
			groupId = groupId,
			action = AlarmAction.ACTION_BLOCKING,
		)

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(constTimeProvider.snoozeTime)

		val scheduleResult = alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = groupName,
			triggerTime = triggerTime,
			action = AlarmAction.ACTION_USING,
		)

		return when {
			scheduleResult.isSuccess -> {
				val stateResult = appGroupRepository.updateAppGroupState(
					groupId = groupId,
					appGroupState = AppGroupState.Using,
					startTime = startTime,
					endTime = triggerTime,
				)

				when {
					stateResult.isSuccess -> {
						// 성공적으로 업데이트됨
					}

					stateResult.isFailure -> {
						val exception = stateResult.exceptionOrNull()
						return BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 그룹 상태 업데이트 실패")))
					}
				}

				val snoozeResult = appGroupRepository.insertSnooze(
					groupId = groupId,
				)

				when {
					snoozeResult.isSuccess -> {
						BrakeResult.Success(triggerTime)
					}

					snoozeResult.isFailure -> {
						val exception = snoozeResult.exceptionOrNull()
						BrakeResult.Error(LocalApiCallError(exception ?: Exception("스누즈 삽입 실패")))
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
