package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.SetBlockingAlarmUseCaseError
import java.time.LocalDateTime

interface SetBlockingAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
	): BrakeResult<LocalDateTime, SetBlockingAlarmUseCaseError>
}
