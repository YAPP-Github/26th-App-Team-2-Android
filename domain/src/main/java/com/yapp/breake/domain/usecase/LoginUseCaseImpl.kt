package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.LoginRepository
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class LoginUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
	@Named("FakeLoginRepo") private val fakeLoginRepository: LoginRepository,
	private val userTokenRepository: UserTokenRepository,
) : LoginUseCase {

	override operator fun invoke(
		authAccessToken: String,
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserTokenStatus> = fakeLoginRepository.flowLogin(
		provider = provider,
		authorizationCode = authAccessToken,
		onError = onError,
	).map {
		userTokenRepository.updateUserToken(
			userAccessToken = it.accessToken,
			userRefreshToken = it.refreshToken,
			userStatus = it.status,
			onError = onError,
		)
		it.status
	}
}
