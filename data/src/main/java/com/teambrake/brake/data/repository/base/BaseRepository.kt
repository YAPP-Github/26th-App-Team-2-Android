package com.teambrake.brake.data.repository.base

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow

open class BaseRepository(
	private val tokenLocalDataSource: TokenLocalDataSource,
) {
	/**
	 * UserStatus에 따라 서버 연결 여부를 결정합니다.
	 * ACTIVE, HALF_SIGNUP: 서버 연결 시도
	 * INACTIVE, OFFLINE: 로컬에서만 실행
	 *
	 * @param runIfOnline 온라인 상태일 때 실행할 블록
	 * @param runIfOffline 오프라인 상태일 때 실행할 블록
	 * @return 실행 결과
	 */
	protected suspend fun <T> executeWithStatusCheck(
		runIfOnline: suspend () -> T,
		runIfOffline: (suspend () -> T),
	): T {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
		).firstOrNull() ?: UserStatus.INACTIVE

		return when (status) {
			UserStatus.ACTIVE, UserStatus.HALF_SIGNUP -> {
				runIfOnline()
			}
			UserStatus.INACTIVE, UserStatus.OFFLINE -> {
				runIfOffline.invoke()
			}
		}
	}

	/**
	 * Flow를 반환하는 작업에 대한 상태 체크
	 */
	protected fun <T> executeFlowWithStatusCheck(
		flowProvider: () -> Flow<T>,
		offlineFlowProvider: () -> Flow<T>,
	): Flow<T> = flow {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
		).firstOrNull() ?: UserStatus.INACTIVE

		when (status) {
			UserStatus.ACTIVE, UserStatus.HALF_SIGNUP -> {
				flowProvider().collect { emit(it) }
			}
			UserStatus.INACTIVE, UserStatus.OFFLINE -> {
				offlineFlowProvider().collect { emit(it) }
			}
		}
	}

	/**
	 * 현재 사용자가 온라인 상태인지 확인
	 */
	protected suspend fun isOnlineStatus(): Boolean {
		val status = tokenLocalDataSource.getUserStatus(
			onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
		).firstOrNull() ?: UserStatus.INACTIVE

		return status == UserStatus.ACTIVE || status == UserStatus.HALF_SIGNUP
	}
}
