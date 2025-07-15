package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow

interface LocalTokenRepository {
	suspend fun updateUserToken(
		userAccessToken: String? = null,
		userRefreshToken: String? = null,
		userStatus: UserStatus? = null,
		onError: suspend (Throwable) -> Unit,
	)

	fun getUserAccessToken(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserRefreshToken(onError: suspend (Throwable) -> Unit): Flow<String>

	suspend fun updateAuthCode(
		authCode: String?,
		onError: suspend (Throwable) -> Unit,
	)

	fun getAuthCode(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserStatus(onError: suspend (Throwable) -> Unit): Flow<UserStatus>
}
