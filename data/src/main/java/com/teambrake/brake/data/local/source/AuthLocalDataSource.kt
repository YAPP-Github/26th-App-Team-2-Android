package com.teambrake.brake.data.local.source

import kotlinx.coroutines.flow.Flow

interface AuthLocalDataSource {
	suspend fun updateAuthCode(authCode: String?, onError: suspend (Throwable) -> Unit)

	fun getAuthCode(onError: suspend (Throwable) -> Unit): Flow<String>

	suspend fun clearAuthCode(onError: suspend (Throwable) -> Unit)
}
