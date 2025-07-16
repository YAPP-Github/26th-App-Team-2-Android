package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.local.AuthLocalDataSource
import com.yapp.breake.data.local.TokenLocalDataSource
import com.yapp.breake.data.remote.source.TokenRemoteDataSource
import com.yapp.breake.domain.repository.TokenRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
	private val tokenRemoteDataSource: TokenRemoteDataSource,
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val authLocalDataSource: AuthLocalDataSource,
) : TokenRepository {

	override fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = tokenRemoteDataSource.getTokens(
		provider = provider,
		authorizationCode = authorizationCode,
		onError = onError,
	).onEach {
		// authCode 습득 성공 시 토큰과 유저 상태 (회원, 비회원) 저장
		tokenLocalDataSource.updateUserToken(
			userAccessToken = it.accessToken,
			userRefreshToken = it.refreshToken,
			userStatus = it.status,
			onError = onError,
		)
	}.onEach {
		// 만약 유저 토큰 상태가 HALF_SIGNUP이라면 authCode를 로컬에 저장
		if (it.status == UserStatus.HALF_SIGNUP) {
			authLocalDataSource.updateAuthCode(
				authCode = authorizationCode,
				onError = onError,
			)
			// 5분 후에 authCode 자동 삭제
			@OptIn(DelicateCoroutinesApi::class)
			GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
				delay(5 * 60 * 1000L)
				authLocalDataSource.clearAuthCode(onError = onError)
			}
		}
	}

	override fun getRemoteTokensRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		authLocalDataSource.getAuthCode(onError = onError).collect { authCode ->
			getRemoteTokens(
				provider = provider,
				authorizationCode = authCode,
				onError = onError,
			).collect { token ->
				emit(token)
			}
		}
	}

	override suspend fun getLocalAccessToken(onError: suspend (Throwable) -> Unit): Flow<String> =
		tokenLocalDataSource.getUserAccessToken(onError = onError)

	override suspend fun clearLocalTokens(onError: suspend (Throwable) -> Unit) {
		authLocalDataSource.updateAuthCode(
			authCode = null,
			onError = onError,
		)
		tokenLocalDataSource.updateUserToken(
			userAccessToken = null,
			userRefreshToken = null,
			userStatus = UserStatus.INACTIVE,
			onError = onError,
		)
	}

	override suspend fun clearLocalAuthCode(onError: suspend (Throwable) -> Unit) {
		authLocalDataSource.clearAuthCode(onError = onError)
	}

	override val canGetLocalTokensRetry
		get() = runBlocking {
			var isAvailable = true
			authLocalDataSource.getAuthCode(
				onError = { isAvailable = false },
			).collect()
			isAvailable
		}
}
