package com.yapp.breake.domain.usecase

/**
 * 카카오 로그인을 통해 AuthCode를 캐시했지만 서비스 서버에서 토큰을 받지 못한 경우, 캐시된 AuthCode로 로그인 시도하는 UseCase
 */
// 미사용
interface LoginWithCachedAuthCodeUseCase {
	suspend operator fun invoke(provider: String, onError: suspend (Throwable) -> Unit)
}
