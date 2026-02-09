package com.teambrake.brake.domain.repository

import com.teambrake.brake.core.model.user.UserName
import kotlinx.coroutines.flow.Flow

interface NicknameRepository {
	/**
	 * 사용자 닉네임을 Flow로 가져오는 메서드
	 *
	 * 온라인 상태일 때는 원격에서, 오프라인 상태일 때는 로컬에서 닉네임을 가져옴
	 * UI에서 실시간으로 닉네임 변경을 관찰하기 위해 사용
	 *
	 * @return [Flow]로 감싸진 닉네임 [String]
	 * @throws Exception 닉네임 가져오기 실패 시
	 */
	fun getNickname(): Flow<String>

	suspend fun saveLocalUserName(
		nickname: String,
	): Result<Unit>

	/**
	 * 사용자 이름을 업데이트하고 로컬에 저장하는 메서드
	 *
	 * @param nickname 새로 설정할 사용자 이름
	 * @return [Result]로 감싸진 [UserName] 객체
	 */
	suspend fun updateUserName(
		nickname: String,
	): Result<UserName>

	/**
	 * 로컬 사용자 저장소를 비우는 메서드
	 *
	 * @return [Result]로 감싸진 Unit
	 */
	suspend fun clearLocalName(): Result<Unit>

}
