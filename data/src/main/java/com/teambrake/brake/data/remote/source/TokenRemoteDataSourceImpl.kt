package com.teambrake.brake.data.remote.source

import com.skydoves.sandwich.suspendOnFailure
import com.skydoves.sandwich.suspendOnSuccess
import com.teambrake.brake.data.remote.model.LoginRequest
import com.teambrake.brake.data.remote.model.LoginResponse
import com.teambrake.brake.data.remote.model.RefreshRequest
import com.teambrake.brake.data.remote.model.RefreshResponse
import com.teambrake.brake.data.remote.retrofit.RetrofitBrakeApi
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
			Timber.e("$this")
			onError(Throwable("서버 연결에 문제가 있습니다"))
		}
	}

	override fun refreshTokens(
		refreshToken: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<RefreshResponse> = flow {
		Timber.d("refreshTokens called with refreshToken: $refreshToken")
		retrofitBrakeApi.refreshTokens(RefreshRequest(refreshToken)).suspendOnSuccess {
			Timber.d("refreshTokens called with refreshToken: $refreshToken")
			emit(this.data)
		}.suspendOnFailure {
			Timber.e("refreshTokens failed")
			onError(Throwable("서버 연결에 문제가 있습니다"))
		}
	}

	override suspend fun logoutAccount(
		accessToken: String,
		onError: suspend (Throwable) -> Unit,
	) {
		retrofitBrakeApi.logoutAuth(accessToken).suspendOnSuccess {
			Timber.d("logoutAccount successful")
		}.suspendOnFailure {
			Timber.e("logoutAccount failed")
			onError(Throwable("로그아웃 중 오류가 발생했습니다"))
		}
	}
}
