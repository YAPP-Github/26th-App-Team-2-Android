package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.CreateNewGroupUseCaseError

interface CreateNewGroupUseCase {
	suspend operator fun invoke(group: AppGroup): BrakeResult<Unit, CreateNewGroupUseCaseError>
}
