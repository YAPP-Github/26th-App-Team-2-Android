package com.teambrake.brake.data.repository

import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.local.source.UserLocalDataSource
import com.teambrake.brake.data.remote.source.AccountRemoteDataSource
import com.teambrake.brake.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SessionRepositoryImpl @Inject constructor(
	private val userLocalDataSource: UserLocalDataSource,
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val accountRemoteDataSource: AccountRemoteDataSource,
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

	override suspend fun clearEntireDataStore(onError: suspend (Throwable) -> Unit) {
		userLocalDataSource.clearUserInfo(onError = onError)
		tokenLocalDataSource.clearUserToken(onError = onError)
	}

	override suspend fun clearRemoteAccount(onError: suspend (Throwable) -> Unit) {
		accountRemoteDataSource.deleteAccount(onError = onError)
	}
}
