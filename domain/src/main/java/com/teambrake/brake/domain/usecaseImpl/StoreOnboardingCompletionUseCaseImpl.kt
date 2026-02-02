package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.StoreOnboardingCompletionUseCaseError
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.usecase.StoreOnboardingCompletionUseCase
import javax.inject.Inject

class StoreOnboardingCompletionUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
) : StoreOnboardingCompletionUseCase {
	override suspend fun invoke(
		isComplete: Boolean,
	): BrakeResult<Unit, StoreOnboardingCompletionUseCaseError> {
		val result = authRepository.updateOnboardingFlag(
			isComplete = isComplete,
		)

		return when {
			result.isSuccess -> {
				BrakeResult.Success(Unit)
			}

			result.isFailure -> {
				val exception = result.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Exception("온보딩 완료 상태 저장 실패")))
			}

			else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
		}
	}
}
