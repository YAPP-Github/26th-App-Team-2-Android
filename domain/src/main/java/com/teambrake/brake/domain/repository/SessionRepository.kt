package com.teambrake.brake.domain.repository

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError

interface SessionRepository {
	suspend fun updateLocalOnboardingFlag(isComplete: Boolean): BrakeResult<Unit, ApiCallError>

	suspend fun getOnboardingFlag(): BrakeResult<Boolean, LocalApiCallError>

	suspend fun clearEntireDataStore(): BrakeResult<Unit, ApiCallError>

	suspend fun clearRemoteAccount(): BrakeResult<Unit, ApiCallError>
}
