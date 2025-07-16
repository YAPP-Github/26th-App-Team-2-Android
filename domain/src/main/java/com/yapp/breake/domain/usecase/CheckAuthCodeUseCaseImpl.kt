package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Named

class CheckAuthCodeUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : CheckAuthCodeUseCase {
	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Boolean = tokenRepository.isLoginRetryAvailable
}
