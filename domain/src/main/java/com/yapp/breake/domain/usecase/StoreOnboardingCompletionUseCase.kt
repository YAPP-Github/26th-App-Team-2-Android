package com.yapp.breake.domain.usecase

interface StoreOnboardingCompletionUseCase {
	suspend operator fun invoke(isComplete: Boolean, onError: suspend (Throwable) -> Unit)
}
