package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.RemoteLoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeRemoteLoginRepositoryImpl @Inject constructor() : RemoteLoginRepository {
	override fun flowLogin(
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
}
