package com.teambrake.brake.data.test.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeNicknameRepositoryImpl @Inject constructor() : NicknameRepository {

	override fun getRemoteUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> = flow {
		emit(
			UserName(
				nickname = "FakeUser",
				state = UserStatus.ACTIVE,
			),
		)
	}

	override fun getLocalUserName(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}

	override suspend fun saveLocalUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}

	override fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName> = flow {
		emit(
			UserName(
				nickname = nickname,
				state = UserStatus.ACTIVE,
			),
		)
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}
}
