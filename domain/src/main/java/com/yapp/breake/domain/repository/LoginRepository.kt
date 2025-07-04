package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserToken
import kotlinx.coroutines.flow.Flow

interface LoginRepository {
	suspend fun flowLogin(
		provider: String,
		authorizationCode: String,
	): Flow<UserToken>
}
