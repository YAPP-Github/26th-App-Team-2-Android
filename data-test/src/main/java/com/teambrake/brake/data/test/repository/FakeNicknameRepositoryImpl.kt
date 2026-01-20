package com.teambrake.brake.data.test.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.success.AuthStatusSuccess
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeNicknameRepositoryImpl @Inject constructor() : NicknameRepository {

	override suspend fun getRemoteUserName(): BrakeResult<AuthStatusSuccess<UserName>, ApiCallError> {
		TODO("Not yet implemented")
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

	override suspend fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): BrakeResult<AuthStatusSuccess<UserName>, ApiCallError> {
		TODO("Not yet implemented")
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		// Fake 구현체에서는 아무 동작도 하지 않음
	}
}
