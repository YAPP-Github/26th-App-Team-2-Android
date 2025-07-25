package com.yapp.breake.domain.repository

import kotlinx.coroutines.flow.Flow

interface SessionRepository {
	suspend fun updateLocalOnboardingFlag(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	)

	fun getOnboardingFlag(onError: suspend (Throwable) -> Unit): Flow<Boolean>
}
