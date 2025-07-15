package com.yapp.breake.data.repository

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.api.LoginApi
import com.yapp.breake.data.api.model.LoginRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.domain.repository.RemoteLoginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class RemoteLoginRepositoryImpl @Inject constructor(
	private val loginApi: LoginApi,
) : RemoteLoginRepository {

	override fun flowLogin(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		loginApi.getTokens(
			LoginRequest(
				provider = provider,
				authorizationCode = authorizationCode,
			),
		).suspendOnSuccess {
			emit(this.data.toData())
		}.suspendOnFailure {
			onError(Throwable("서버 연결에 문제가 있습니다"))
		}
	}
}
