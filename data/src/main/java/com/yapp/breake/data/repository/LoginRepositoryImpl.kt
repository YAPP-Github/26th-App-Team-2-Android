package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.api.LoginApi
import com.yapp.breake.data.api.model.LoginRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.domain.repository.LoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class LoginRepositoryImpl @Inject constructor(
	private val loginApi: LoginApi,
) : LoginRepository {

	override suspend fun flowLogin(
		provider: String,
		authorizationCode: String,
	): Flow<UserToken> = flow {
		emit(
			loginApi.getTokens(
				LoginRequest(
					provider = provider,
					authorizationCode = authorizationCode,
				),
			),
		)
	}.map { it.toData() }
}
