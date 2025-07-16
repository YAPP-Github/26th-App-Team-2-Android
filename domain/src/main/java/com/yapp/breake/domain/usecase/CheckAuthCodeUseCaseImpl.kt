package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Named

class CheckAuthCodeUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
) : CheckAuthCodeUseCase {
	override suspend fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Boolean = loginRepository.isLoginRetryAvailable
}
