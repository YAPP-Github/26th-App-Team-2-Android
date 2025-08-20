package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserToken
import kotlinx.coroutines.flow.Flow

interface TokenRepository {

	/**
	 * 서버에서 로그인 토큰을 가져오는 메서드
	 *
	 * 카카오 인가 코드 발급 직후 사용되는 메서드
	 *
	 * @param provider 로그인 제공자 (현재 Kakao 고정)
	 * @param authorizationCode 인증 코드
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserToken] 객체
	 */
	fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken>

	/**
	 * 서버에서 로그인 토큰을 재시도하여 가져오는 메서드
	 *
	 * 카카오 인가 코드 발급 후 최초 로그인 실패 시 사용되는 메서드
	 *
	 * @param provider 로그인 제공자 (현재 Kakao 고정)
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserToken] 객체
	 */
	fun getRemoteTokensRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken>

	/**
	 * 로컬에 저장된 토큰을 모두 초기화하는 메서드
	 *
	 * 로컬에 저장된 AuthCode, AccessToken, RefreshToken 모두 초기화
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun clearLocalTokens(
		onError: suspend (Throwable) -> Unit,
	)

	suspend fun refreshTokens(
		onError: suspend (Throwable) -> Unit,
	)

	/**
	 * AuthCode 비우기 메서드
	 *
	 * 로그인 성공 후 로컬에 AuthCode를 삭제
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun clearLocalAuthCode(
		onError: suspend (Throwable) -> Unit,
	)

	/**
	 * 원격 계정 로그아웃 메서드
	 *
	 * 저장된 AccessToken을 이용하여 서버에 로그아웃 요청
	 */
	fun logoutRemoteAccount()
}
