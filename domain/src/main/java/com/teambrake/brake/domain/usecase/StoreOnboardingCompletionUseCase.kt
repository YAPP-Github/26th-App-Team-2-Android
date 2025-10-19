package com.teambrake.brake.domain.usecase

interface StoreOnboardingCompletionUseCase {
	suspend operator fun invoke(isComplete: Boolean, onError: suspend (Throwable) -> Unit)
}
