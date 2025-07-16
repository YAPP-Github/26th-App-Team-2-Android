package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * 카카오 로그인 이후 AuthCode를 이용하여 로그인하는 UseCase
 *
 * 로그인 성공 시 UserStatus를 반환하며, 실패 시 onError 콜백을 호출
 */
class LoginUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
) : LoginUseCase {

	override operator fun invoke(
		authCode: String,
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserStatus> = loginRepository.login(
		provider = provider,
		authorizationCode = authCode,
		onError = onError,
	).map {
		it.status
	}
}
