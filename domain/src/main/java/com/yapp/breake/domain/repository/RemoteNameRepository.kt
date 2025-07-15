package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserName
import kotlinx.coroutines.flow.Flow

interface RemoteNameRepository {
	fun getUserName(onError: suspend (Throwable) -> Unit): Flow<UserName>

	fun updateUserName(
		authCode: String,
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserName>
}
