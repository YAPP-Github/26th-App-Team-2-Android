package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.SessionRepository
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
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
