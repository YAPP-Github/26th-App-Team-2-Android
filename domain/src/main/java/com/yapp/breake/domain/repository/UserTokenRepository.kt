package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserTokenStatus
import kotlinx.coroutines.flow.Flow

interface UserTokenRepository {
	suspend fun updateUserToken(
		userAccessToken: String? = null,
		userRefreshToken: String? = null,
		userStatus: UserTokenStatus? = null,
	)

	fun getUserAccessToken(): Flow<String>

	fun getUserRefreshToken(): Flow<String>

	fun getUserStatus(): Flow<UserTokenStatus>
}
