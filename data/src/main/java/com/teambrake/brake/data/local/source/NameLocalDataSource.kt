package com.teambrake.brake.data.local.source

import kotlinx.coroutines.flow.Flow

interface NameLocalDataSource {
	suspend fun updateNickname(nickname: String): Boolean
	suspend fun clearNickname(): Boolean
	fun getNickname(): Flow<String>
}
