package com.teambrake.brake.domain.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.success.ModeSuccess
import kotlinx.coroutines.flow.Flow

interface NicknameRepository {
	/**
	 * 서버에서 사용자 이름을 가져오는 메서드
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [BrakeResult]로 감싸진 [ModeSuccess]<[UserName]> 또는 [ApiCallError] 객체
	 */
	suspend fun getRemoteUserName(): BrakeResult<ModeSuccess<UserName>, ApiCallError>

	fun getLocalUserName(onError: suspend (Throwable) -> Unit): Flow<String>

	suspend fun saveLocalUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	)

	/**
	 * 사용자 이름을 업데이트하고 로컬에 저장하는 메서드
	 *
	 * @param nickname 새로 설정할 사용자 이름
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [BrakeResult]로 감싸진 [ModeSuccess]<[UserName]> 또는 [ApiCallError] 객체
	 */
	suspend fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): BrakeResult<ModeSuccess<UserName>, ApiCallError>

	/**
	 * 로컬 사용자 저장소를 비우는 메서드
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun clearLocalName(onError: suspend (Throwable) -> Unit)

}
