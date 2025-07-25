package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.SessionRepository
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
