package com.yapp.breake.data.remote.source

import com.yapp.breake.data.remote.model.LoginResponse
import kotlinx.coroutines.flow.Flow

interface TokenRemoteDataSource {
	fun getTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<LoginResponse>
}
