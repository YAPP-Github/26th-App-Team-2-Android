package com.teambrake.brake.data.local.source

import com.teambrake.brake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
	suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserStatus?,
		onError: suspend (Throwable) -> Unit,
	)

	fun getUserAccessToken(): Flow<String>

	fun getUserRefreshToken(): Flow<String>

	fun getUserStatus(): Flow<UserStatus>

	suspend fun clearUserToken(onError: suspend (Throwable) -> Unit)
}
