package com.yapp.breake.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserTokenRepository {
	suspend fun saveUserToken(
		userAccessToken: String? = null,
		userRefreshToken: String? = null,
	): Flow<Unit>

	suspend fun getUserAccessToken(): Flow<String>

	suspend fun getUserRefreshToken(): Flow<String>
}
