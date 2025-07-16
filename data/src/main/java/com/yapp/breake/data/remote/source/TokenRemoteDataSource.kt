package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.remote.model.LoginRequest
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import com.yapp.breake.data.repository.mapper.toData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class TokenRemoteDataSource @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) {
	fun getTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		Timber.d("getTokens called with provider: $provider, authorizationCode: $authorizationCode")
		retrofitBrakeApi.getTokens(
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
