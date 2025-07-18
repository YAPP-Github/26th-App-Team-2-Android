package com.yapp.breake.domain.usecase

/**
 * AuthCode가 Local에 캐시되어 있는지 확인하는 UseCase
 *
 * 이 UseCase는 로그인 시도 전에 캐싱된 AuthCode가 유효한지 확인하는 데 사용
 *
 * 사용 시나리오:
 * 1. 사용자가 카카오 로그인을 시도하기 전, AuthCode가 Local에 캐시되어 있는지 확인
 * 2. 캐시된 AuthCode가 있다면, 이를 사용하여 카카오 로그인 스킵, [LoginWithCachedAuthCodeUseCase]를 호출
 * 3. 캐시된 AuthCode가 없다면, 카카오 로그인 프로세스를 진행
 *
 * AuthCode가 캐시되는 경우는 앞선 5분 내에 카카오 로그인을 시도했지만 토큰을 발급받지 못한 특수한 경우
 */
// 미사용
interface CheckAuthCodeUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Boolean
}
