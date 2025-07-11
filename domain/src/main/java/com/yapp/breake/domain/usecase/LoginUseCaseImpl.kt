package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.LoginRepository
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
	): Flow<ResponseResult<UserTokenStatus>> = fakeLoginRepository.flowLogin(
		provider = provider,
		authorizationCode = authAccessToken,
	).map { response ->
		when (response) {
			is ResponseResult.Success -> {
				userTokenRepository.updateUserToken(
					userAccessToken = response.data.accessToken,
					userRefreshToken = response.data.refreshToken,
					userStatus = response.data.status,
				)
				ResponseResult.Success(response.data.status)
			}
			is ResponseResult.Error -> {
				ResponseResult.Error(response.message)
			}
			is ResponseResult.Exception -> {
				ResponseResult.Exception(response.exception)
			}
		}
	}
}
