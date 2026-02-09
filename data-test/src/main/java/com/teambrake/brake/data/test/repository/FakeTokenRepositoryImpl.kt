package com.teambrake.brake.data.test.repository

import com.teambrake.brake.core.model.user.UserToken
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeTokenRepositoryImpl @Inject constructor() : TokenRepository {
	override suspend fun setOfflineModeStatus(): Result<Unit> = Result.success(Unit)

	override fun getUserStatus(): Flow<UserStatus> = flow {
		emit(UserStatus.ACTIVE)
	}

	override fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
	): Flow<UserToken> = flow {
		emit(
			UserToken(
				accessToken = "FakeAccessToken",
				refreshToken = "FakeRefreshToken",
				status = UserStatus.HALF_SIGNUP,
			),
		)
	}

	override fun getRemoteTokensRetry(provider: String): Flow<UserToken> = getRemoteTokens(
		provider = provider,
		authorizationCode = "Fake",
	)

	override suspend fun clearLocalTokens(): Result<Unit> = Result.success(Unit)

	override suspend fun refreshTokens(): Result<Unit> = Result.success(Unit)

	override suspend fun clearLocalAuthCode(): Result<Unit> = Result.success(Unit)

	override suspend fun logoutRemoteAccount(): Result<Unit> = Result.success(Unit)
}
