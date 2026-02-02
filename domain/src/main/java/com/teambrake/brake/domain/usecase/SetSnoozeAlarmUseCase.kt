package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.SetSnoozeAlarmUseCaseError
import java.time.LocalDateTime

interface SetSnoozeAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
		groupName: String,
	): BrakeResult<LocalDateTime, SetSnoozeAlarmUseCaseError>
}
