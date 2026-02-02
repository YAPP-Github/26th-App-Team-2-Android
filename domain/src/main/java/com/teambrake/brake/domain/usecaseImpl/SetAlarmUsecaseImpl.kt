package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.common.AlarmAction
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.SetAlarmUseCaseError
import com.teambrake.brake.domain.repository.AlarmScheduler
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.etc.ConstTimeProvider
import com.teambrake.brake.domain.usecase.SetAlarmUseCase
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
	): BrakeResult<LocalDateTime, SetAlarmUseCaseError> {
		val (action, time) = when (appGroupState) {
			AppGroupState.Using -> AlarmAction.ACTION_USING to constTimeProvider.getTime(second.toLong())
			AppGroupState.Blocking -> AlarmAction.ACTION_BLOCKING to constTimeProvider.blockingTime
			else -> {
				return BrakeResult.Error(LocalApiCallError(IllegalStateException("알람을 예약하지 않는 상태입니다.")))
			}
		}

		val startTime = LocalDateTime.now()
		val triggerTime = startTime.plusSeconds(time)

		val scheduleResult = alarmScheduler.scheduleAlarm(
			groupId = groupId,
			groupName = groupName,
			triggerTime = triggerTime,
			action = action,
		)

		return when {
			scheduleResult.isSuccess -> {
				if (appGroupState == AppGroupState.Using) {
					val sessionResult = appGroupRepository.updateGroupSessionInfo(
						groupId = groupId,
						goalMinutes = second,
						sessionStartTime = startTime,
					)

					when {
						sessionResult.isSuccess -> {
							// 성공적으로 업데이트됨
						}

						sessionResult.isFailure -> {
							val exception = sessionResult.exceptionOrNull()
							return BrakeResult.Error(LocalApiCallError(exception ?: Exception("그룹 세션 정보 업데이트 실패")))
						}
					}
				}

				val stateResult = appGroupRepository.updateAppGroupState(
					groupId = groupId,
					appGroupState = if (isUsingApp) {
						AppGroupState.SnoozeBlocking
					} else {
						appGroupState
					},
					startTime = startTime,
					endTime = triggerTime,
				)

				when {
					stateResult.isSuccess -> {
						BrakeResult.Success(triggerTime)
					}

					stateResult.isFailure -> {
						val exception = stateResult.exceptionOrNull()
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
