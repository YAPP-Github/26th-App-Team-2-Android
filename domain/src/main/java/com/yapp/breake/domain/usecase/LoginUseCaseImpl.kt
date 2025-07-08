package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.LoginRepository
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject
import javax.inject.Named

class LoginUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
	@Named("FakeLoginRepo") private val fakeLoginRepository: LoginRepository,
	private val userTokenRepository: UserTokenRepository,
) : LoginUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override suspend operator fun invoke(
		authAccessToken: String,
		provider: String,
	): Flow<UserTokenStatus> = fakeLoginRepository.flowLogin(
		provider = provider,
		authorizationCode = authAccessToken,
	).flatMapLatest {
		println("LoginUseCaseImpl: UserToken received: $it")
		userTokenRepository.saveUserToken(
			userAccessToken = it.accessToken,
			userRefreshToken = it.refreshToken,
			userStatus = it.status,
		).flatMapConcat {
			userTokenRepository.getUserStatus().apply {
				println("LoginUseCaseImpl: UserTokenStatus: $it")
			}
		}
	}
}
