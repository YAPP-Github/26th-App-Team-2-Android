package com.yapp.breake.data.remote.source

import com.yapp.breake.data.remote.model.LoginResponse
import com.yapp.breake.data.remote.model.RefreshResponse
import kotlinx.coroutines.flow.Flow

interface TokenRemoteDataSource {
	fun getTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<LoginResponse>

	fun refreshTokens(
		refreshToken: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<RefreshResponse>

	suspend fun logoutAccount(
		accessToken: String,
		onError: suspend (Throwable) -> Unit,
	)
}
