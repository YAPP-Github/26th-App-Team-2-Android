package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.NicknameRepository
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

	override fun updateUserName(
		accessToken: String,
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

	override suspend fun getLocalAccessToken(onError: suspend (Throwable) -> Unit): Flow<String> {
		TODO("Not yet implemented")
	}

	override suspend fun clearLocalUserStorage(onError: suspend (Throwable) -> Unit) {
		TODO("Not yet implemented")
	}
}
