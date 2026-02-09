package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.GrantNewGroupIdUseCaseError

interface GrantNewGroupIdUseCase {
	suspend operator fun invoke(): BrakeResult<Long, GrantNewGroupIdUseCaseError>
}
