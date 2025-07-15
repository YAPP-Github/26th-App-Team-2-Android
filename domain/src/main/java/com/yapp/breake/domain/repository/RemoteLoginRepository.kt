package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserToken
import kotlinx.coroutines.flow.Flow

interface RemoteLoginRepository {
	fun flowLogin(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken>
}
