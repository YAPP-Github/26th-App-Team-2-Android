package com.teambrake.brake.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
	/**
	 * 로컬 온보딩 플래그를 업데이트하는 메서드
	 *
	 * @param isComplete 온보딩 완료 여부
	 * @throws Exception 업데이트 실패 시
	 */
	suspend fun updateOnboardingFlag(isComplete: Boolean): Result<Unit>

	/**
	 * 온보딩 플래그를 가져오는 메서드
	 *
	 * @return [Flow]로 감싸진 온보딩 완료 여부
	 * @throws Exception 가져오기 실패 시
	 */
	fun getOnboardingFlag(): Flow<Boolean>

	/**
	 * 로컬 인증 데이터 스토어를 초기화하는 메서드
	 *
	 * @return [Result]로 감싸진 Unit
	 */
	suspend fun clearAuthDataStore(): Result<Unit>

	/**
	 * 원격 계정을 삭제하는 메서드
	 *
	 * @return [Result]로 감싸진 Unit
	 */
	suspend fun clearRemoteAccount(): Result<Unit>
}
