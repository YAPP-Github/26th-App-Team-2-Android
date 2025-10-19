package com.teambrake.brake.data.test.repository

import com.teambrake.brake.core.model.user.UserToken
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeTokenRepositoryImpl @Inject constructor() : TokenRepository {
	override fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		emit(
			UserToken(
				accessToken = "FakeAccessToken",
				refreshToken = "FakeRefreshToken",
				status = UserStatus.HALF_SIGNUP,
			),
		)
	}

	override fun getRemoteTokensRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = getRemoteTokens(
		provider = provider,
		authorizationCode = "Fake",
		onError = onError,
	)

	override suspend fun clearLocalTokens(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체 에서는 아무 동작도 하지 않음
	}

	override suspend fun refreshTokens(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체 에서는 아무 동작도 하지 않음
	}

	override suspend fun clearLocalAuthCode(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}

	override fun logoutRemoteAccount() {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}
}
