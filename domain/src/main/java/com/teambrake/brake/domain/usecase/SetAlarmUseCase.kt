package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.SetAlarmUseCaseError
import java.time.LocalDateTime

interface SetAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
		groupName: String,
		appGroupState: AppGroupState,
		second: Int = 0,
		isUsingApp: Boolean = false,
	): BrakeResult<LocalDateTime, SetAlarmUseCaseError>
}
