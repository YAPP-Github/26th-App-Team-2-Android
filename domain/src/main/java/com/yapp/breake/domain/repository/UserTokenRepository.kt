package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserTokenStatus
import kotlinx.coroutines.flow.Flow

interface UserTokenRepository {
	suspend fun saveUserToken(
		userAccessToken: String? = null,
		userRefreshToken: String? = null,
		userStatus: UserTokenStatus? = null,
	): Flow<Unit>

	suspend fun getUserAccessToken(): Flow<String>

	suspend fun getUserRefreshToken(): Flow<String>

	suspend fun getUserStatus(): Flow<UserTokenStatus>
}
