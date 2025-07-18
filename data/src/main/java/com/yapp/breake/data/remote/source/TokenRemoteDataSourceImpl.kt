package com.yapp.breake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.yapp.breake.data.remote.model.LoginRequest
import com.yapp.breake.data.remote.model.LoginResponse
import com.yapp.breake.data.remote.retrofit.RetrofitBrakeApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class TokenRemoteDataSourceImpl @Inject constructor(
	private val retrofitBrakeApi: RetrofitBrakeApi,
) : TokenRemoteDataSource {
	override fun getTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<LoginResponse> = flow {
		Timber.d("getTokens called with provider: $provider, authorizationCode: $authorizationCode")
		retrofitBrakeApi.getTokens(
			LoginRequest(
				provider = provider,
				authorizationCode = authorizationCode,
			),
		).suspendOnSuccess {
			emit(this.data)
		}.suspendOnFailure {
			onError(Throwable("서버 연결에 문제가 있습니다"))
		}
	}
}
