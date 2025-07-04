package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
	private val loginRepository: LoginRepository,
) : LoginUseCase {
	override suspend operator fun invoke(
		authAccessToken: String,
		provider: String,
	): Flow<UserToken> =
		loginRepository.flowLogin(
			provider = provider,
			authorizationCode = authAccessToken,
		)
}
