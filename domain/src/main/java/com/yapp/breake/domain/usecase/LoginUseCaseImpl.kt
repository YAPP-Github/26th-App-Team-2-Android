package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.LoginRepository
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Named

class LoginUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
	@Named("FakeLoginRepo") private val fakeLoginRepository: LoginRepository,
	private val userTokenRepository: UserTokenRepository,
) : LoginUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override operator fun invoke(
		authAccessToken: String,
		provider: String,
	): Flow<UserTokenStatus> = fakeLoginRepository.flowLogin(
		provider = provider,
		authorizationCode = authAccessToken,
	).onEach { response ->
//		try {
//			userTokenRepository.updateUserToken(
//				userAccessToken = response.accessToken,
//				userRefreshToken = response.refreshToken,
//				userStatus = response.status,
//			)
//		} catch (_: Exception) {
//		}
	}.map { it.status }
}
