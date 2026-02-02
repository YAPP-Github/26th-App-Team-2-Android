package com.teambrake.brake.data.test.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeNicknameRepositoryImpl @Inject constructor() : NicknameRepository {

	override fun getNickname(): Flow<String> = flow {
		emit("FakeUser")
	}

	override suspend fun saveLocalUserName(
		nickname: String,
	): Result<Unit> = Result.success(Unit)

	override suspend fun updateUserName(
		nickname: String,
	): Result<UserName> = Result.success(
		UserName(
			nickname = nickname,
			state = UserStatus.ACTIVE,
		),
	)

	override suspend fun clearLocalName(): Result<Unit> = Result.success(Unit)
}
