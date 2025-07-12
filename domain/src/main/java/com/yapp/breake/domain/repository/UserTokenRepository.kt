package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserTokenStatus
import kotlinx.coroutines.flow.Flow

interface UserTokenRepository {
	suspend fun updateUserToken(
		userAccessToken: String? = null,
		userRefreshToken: String? = null,
		userStatus: UserTokenStatus? = null,
		onError: suspend (Throwable) -> Unit,
	)

	fun getUserAccessToken(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserRefreshToken(onError: suspend (Throwable) -> Unit): Flow<String>

	fun getUserStatus(onError: suspend (Throwable) -> Unit): Flow<UserTokenStatus>
}
