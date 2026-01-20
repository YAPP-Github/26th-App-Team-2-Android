package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.usecase.StoreOnboardingCompletionUseCase
import javax.inject.Inject

class StoreOnboardingCompletionUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
) : StoreOnboardingCompletionUseCase {
	override suspend fun invoke(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	) {
		authRepository.updateLocalOnboardingFlag(
			isComplete = isComplete,
		)
	}
}
