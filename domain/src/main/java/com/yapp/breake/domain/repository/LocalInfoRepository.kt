package com.yapp.breake.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalInfoRepository {
	suspend fun updateNickname(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	)

	fun getNickname(
		onError: suspend (Throwable) -> Unit,
	): Flow<String>
}
