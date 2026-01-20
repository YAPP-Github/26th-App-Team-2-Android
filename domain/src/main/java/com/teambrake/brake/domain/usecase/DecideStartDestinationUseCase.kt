package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.error.DecideStartDestinationUseCaseError
import com.teambrake.brake.domain.model.result.success.AuthStatusSuccess

interface DecideStartDestinationUseCase {
	suspend operator fun invoke(): BrakeResult<AuthStatusSuccess<Destination>, DecideStartDestinationUseCaseError>
}
