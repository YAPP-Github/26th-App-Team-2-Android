package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeLoginRepositoryImpl @Inject constructor() : LoginRepository {
	override fun login(
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

	override fun loginRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = login(
		provider = provider,
		authorizationCode = "Fake",
		onError = onError,
	)

	override suspend fun logout(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체 에서는 아무 동작도 하지 않음
	}

	override suspend fun clearAuthCode(onError: suspend (Throwable) -> Unit) {
		TODO("Not yet implemented")
	}

	override val isLoginRetryAvailable: Boolean = true
}
