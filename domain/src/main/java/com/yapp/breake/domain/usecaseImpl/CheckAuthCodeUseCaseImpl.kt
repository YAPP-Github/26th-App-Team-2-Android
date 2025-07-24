package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.usecase.CheckAuthCodeUseCase
import javax.inject.Inject
import javax.inject.Named

class CheckAuthCodeUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : CheckAuthCodeUseCase {
	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Boolean = tokenRepository.canGetLocalTokensRetry
}
