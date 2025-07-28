package com.yapp.breake.data.local.source

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
	suspend fun updateOnboardingFlag(isComplete: Boolean, onError: suspend (Throwable) -> Unit)

	fun getOnboardingFlag(onError: suspend (Throwable) -> Unit): Flow<Boolean>

	suspend fun clearUserInfo(onError: suspend (Throwable) -> Unit)

	suspend fun updateNickname(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	)

	suspend fun clearNickname(onError: suspend (Throwable) -> Unit)

	fun getNickname(onError: suspend (Throwable) -> Unit): Flow<String>
}
