package com.yapp.breake.data.repository

import com.yapp.breake.data.local.source.UserLocalDataSource
import com.yapp.breake.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
	private val userLocalDataSource: UserLocalDataSource,
) : SessionRepository {

	override suspend fun updateLocalOnboardingFlag(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	) {
		userLocalDataSource.updateOnboardingFlag(
			isComplete = isComplete,
			onError = onError,
		)
	}

	override fun getOnboardingFlag(onError: suspend (Throwable) -> Unit): Flow<Boolean> =
		userLocalDataSource.getOnboardingFlag(onError = onError)
}
