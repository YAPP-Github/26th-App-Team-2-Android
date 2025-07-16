package com.yapp.breake.domain.usecase

import com.yapp.breake.domain.repository.TokenRepository
import javax.inject.Inject
import javax.inject.Named

class LoginWithCachedAuthCodeUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : LoginWithCachedAuthCodeUseCase {
	override suspend fun invoke(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	) {
		if (tokenRepository.isLoginRetryAvailable) {
			// 로그인 재시도 가능 시, 캐시된 authCode로 로그인 시도
			tokenRepository.loginRetry(
				provider = "KAKAO",
				onError = onError,
			)
		} else {
			// 로그인 재시도 불가능 시, 에러 처리
			onError(Throwable("로그인 재시도가 불가능합니다."))
		}
	}
}
