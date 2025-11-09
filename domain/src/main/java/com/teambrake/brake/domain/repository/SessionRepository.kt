package com.teambrake.brake.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
	suspend fun updateLocalOnboardingFlag(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	)

	fun getOnboardingFlag(onError: suspend (Throwable) -> Unit): Flow<Boolean>

	suspend fun clearEntireDataStore(onError: suspend (Throwable) -> Unit)

	suspend fun clearRemoteAccount(onError: suspend (Throwable) -> Unit)
}
