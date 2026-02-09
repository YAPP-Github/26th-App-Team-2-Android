package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ResetAppGroupUseCaseError

interface ResetAppGroupUsecase {
	suspend operator fun invoke(
		appGroup: AppGroup,
	): BrakeResult<Unit, ResetAppGroupUseCaseError>
}
