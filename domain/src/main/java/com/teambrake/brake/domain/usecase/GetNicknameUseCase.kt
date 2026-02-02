package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.GetNicknameUseCaseError
import kotlinx.coroutines.flow.Flow

interface GetNicknameUseCase {
	/**
	 * Retrieves the nickname of the user.
	 *
	 * @return The user's nickname as a BrakeResult within a Flow.
	 */
	operator fun invoke(): Flow<BrakeResult<String, GetNicknameUseCaseError>>
}
