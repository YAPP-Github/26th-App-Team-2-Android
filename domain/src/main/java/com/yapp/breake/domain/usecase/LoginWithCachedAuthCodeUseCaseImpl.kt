package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.LoginRepository
import javax.inject.Inject
import javax.inject.Named

class LoginWithCachedAuthCodeUseCaseImpl @Inject constructor(
	@Named("LoginRepo") private val loginRepository: LoginRepository,
) : LoginWithCachedAuthCodeUseCase {
	override suspend fun invoke(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	) {
		if (loginRepository.isLoginRetryAvailable) {
			// 로그인 재시도 가능 시, 캐시된 authCode로 로그인 시도
			loginRepository.loginRetry(
				provider = "KAKAO",
				onError = onError,
			)
		} else {
			// 로그인 재시도 불가능 시, 에러 처리
			onError(Throwable("로그인 재시도가 불가능합니다."))
		}
	}
}
