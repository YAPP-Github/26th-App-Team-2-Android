package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeLoginRepositoryImpl @Inject constructor() : LoginRepository {
	override fun flowLogin(
		provider: String,
		authorizationCode: String,
	): Flow<UserToken> = flow {
		emit(
			UserToken(
				accessToken = "FakeAccessToken",
				refreshToken = "FakeRefreshToken",
				status = UserTokenStatus.HALF_SIGNUP,
			),
		)
	}
}
