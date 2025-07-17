package com.yapp.breake.data.local.source

import com.yapp.breake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow

interface TokenLocalDataSource {
	suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserStatus?,
		onError: suspend (Throwable) -> Unit,
	)

	fun getUserAccessToken(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserRefreshToken(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserStatus(onError: suspend (Throwable) -> Unit): Flow<UserStatus>

	suspend fun clearUserToken(onError: suspend (Throwable) -> Unit)
}
