package com.yapp.breake.data.local.source

import kotlinx.coroutines.flow.Flow

interface UserLocalDataSource {
	suspend fun updateNickname(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	)

	suspend fun clearNickname(onError: suspend (Throwable) -> Unit)

	fun getNickname(onError: suspend (Throwable) -> Unit): Flow<String>
}
