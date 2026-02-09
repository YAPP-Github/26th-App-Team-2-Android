package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideNextDestinationFromPermissionUseCaseError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DecideNextDestinationFromPermissionUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
) : DecideNextDestinationFromPermissionUseCase {
	override suspend fun invoke(): BrakeResult<Destination, DecideNextDestinationFromPermissionUseCaseError> {
		try {
			val isOnboardingCompleted = authRepository.getOnboardingFlag()
				.catch { e ->
					throw e
				}.first()
			val destination = if (isOnboardingCompleted) {
				Destination.PermissionOrHome
			} else {
				Destination.Onboarding
			}
			return BrakeResult.Success(destination)
		} catch (e: Exception) {
			return BrakeResult.Error(UndefinedExceptionError(e))
		}
	}
}
