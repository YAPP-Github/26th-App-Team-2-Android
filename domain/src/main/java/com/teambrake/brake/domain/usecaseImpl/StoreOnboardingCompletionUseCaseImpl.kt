package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.StoreOnboardingCompletionUseCase
import javax.inject.Inject

class StoreOnboardingCompletionUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
) : StoreOnboardingCompletionUseCase {
	override suspend fun invoke(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	) {
		sessionRepository.updateLocalOnboardingFlag(
			isComplete = isComplete,
			onError = onError,
		)
	}
}
