package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class DecideNextDestinationFromPermissionUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
) : DecideNextDestinationFromPermissionUseCase {
	override fun invoke(onError: suspend (Throwable) -> Unit): Destination = runBlocking {
		sessionRepository.getOnboardingFlag(onError = onError).firstOrNull()
			?.let { isOnboardingCompleted ->
				if (isOnboardingCompleted) {
					Destination.PermissionOrHome
				} else {
					Destination.Onboarding
				}
			} ?: Destination.Onboarding
	}
}
