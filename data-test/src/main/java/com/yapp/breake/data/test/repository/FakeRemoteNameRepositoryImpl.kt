package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.RemoteNameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeRemoteNameRepositoryImpl @Inject constructor() : RemoteNameRepository {

	override fun getUserName(onError: suspend (Throwable) -> Unit): Flow<UserName> = flow {
		emit(
			UserName(
				nickname = "FakeUser",
				state = UserStatus.ACTIVE,
			),
		)
	}

	override fun updateUserName(
		authCode: String,
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
}
