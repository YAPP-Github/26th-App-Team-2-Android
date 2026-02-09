package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideNextDestinationFromPermissionUseCaseError

interface DecideNextDestinationFromPermissionUseCase {
	suspend operator fun invoke(): BrakeResult<Destination, DecideNextDestinationFromPermissionUseCaseError>
}
