package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserToken
import kotlinx.coroutines.flow.Flow

interface LoginRepository {

	/**
	 * 로그인 메서드
	 *
	 * 카카오 인가 코드 발급 직후 사용되는 메서드
	 *
	 * @param provider 로그인 제공자 (현재 Kakao 고정)
	 * @param authorizationCode 인증 코드
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserToken] 객체
	 */
	fun login(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken>

	/**
	 * 로그인 재시도 메서드
	 *
	 * 카카오 인가 코드 발급 후 최초 로그인 실패 시 사용되는 메서드
	 *
	 * @param provider 로그인 제공자 (현재 Kakao 고정)
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserToken] 객체
	 */
	fun loginRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken>

	/**
	 * 로그아웃 메서드
	 *
	 * 로컬에 저장된 AuthCode, AccessToken, RefreshToken 모두 초기화
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun logout(
		onError: suspend (Throwable) -> Unit,
	)

	/**
	 * AuthCode 비우기 메서드
	 *
	 * 로그인 성공 후 로컬에 AuthCode를 삭제
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun clearAuthCode(
		onError: suspend (Throwable) -> Unit,
	)

	/**
	 * 로그인 재시도 가능 여부 확인 메서드
	 *
	 * AuthCode가 로컬에 저장되어 있는지 확인하여 로그인 재시도 가능 여부를 반환
	 *
	 * @return [Boolean] 로그인 재시도 가능 여부
	 */
	val isLoginRetryAvailable: Boolean
}
