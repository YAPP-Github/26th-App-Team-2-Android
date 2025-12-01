package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideNextDestinationFromPermissionUseCaseError
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import javax.inject.Inject

class DecideNextDestinationFromPermissionUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
) : DecideNextDestinationFromPermissionUseCase {
	override suspend fun invoke(): BrakeResult<Destination, DecideNextDestinationFromPermissionUseCaseError> =
		when (val result = sessionRepository.getOnboardingFlag()) {
			is BrakeResult.Success -> {
				val isOnboardingCompleted = result.data
				val destination = if (isOnboardingCompleted) {
					Destination.PermissionOrHome
				} else {
					Destination.Onboarding
				}
				BrakeResult.Success(destination)
			}

			is BrakeResult.Error -> {
				BrakeResult.Error(result.error)
			}
		}
}
