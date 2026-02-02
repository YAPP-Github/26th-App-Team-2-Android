package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.StoreOnboardingCompletionUseCaseError

interface StoreOnboardingCompletionUseCase {
	suspend operator fun invoke(isComplete: Boolean): BrakeResult<Unit, StoreOnboardingCompletionUseCaseError>
}
