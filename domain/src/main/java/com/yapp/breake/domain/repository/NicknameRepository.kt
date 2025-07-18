package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserName
import kotlinx.coroutines.flow.Flow

interface NicknameRepository {
	/**
	 * 서버에서 사용자 이름을 가져오는 메서드
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserName] 객체
	 */
	fun getRemoteUserName(onError: suspend (Throwable) -> Unit): Flow<UserName>

	/**
	 * 사용자 이름을 업데이트하고 로컬에 저장하는 메서드
	 *
	 * @param nickname 새로 설정할 사용자 이름
	 * @param onError 오류 발생 시 호출되는 콜백
	 * @return [Flow]로 감싸진 [UserName] 객체
	 */
	fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName>

	/**
	 * 로컬 사용자 저장소를 비우는 메서드
	 *
	 * @param onError 오류 발생 시 호출되는 콜백
	 */
	suspend fun clearLocalName(onError: suspend (Throwable) -> Unit)
}
