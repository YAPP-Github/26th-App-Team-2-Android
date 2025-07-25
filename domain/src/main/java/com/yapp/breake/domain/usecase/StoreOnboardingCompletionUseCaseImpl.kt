package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.SessionRepository
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
