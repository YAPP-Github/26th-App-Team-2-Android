package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.local.source.AuthLocalDataSource
import com.yapp.breake.data.local.source.TokenLocalDataSource
import com.yapp.breake.data.remote.source.TokenRemoteDataSource
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
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
	).map {
		it.toData()
	}.onEach {
		// authCode 습득 성공 시 토큰과 유저 상태 (회원, 비회원) 저장
		Timber.d("accessToken: ${it.accessToken}, refreshToken: ${it.refreshToken}, status: ${it.status}")
		tokenLocalDataSource.updateUserToken(
			userAccessToken = it.accessToken,
			userRefreshToken = it.refreshToken,
			userStatus = it.status,
			userProvider = provider,
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
			GlobalScope.launch(Dispatchers.IO) {
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

	override suspend fun refreshTokens(onError: suspend (Throwable) -> Unit) {
		val refreshToken = tokenLocalDataSource.getUserRefreshToken(onError).firstOrNull()
		refreshToken?.let {
			tokenRemoteDataSource.refreshTokens(
				refreshToken = refreshToken,
				onError = onError,
			).map {
				it.toData()
			}.onEach {
				// 토큰 갱신 성공 시 로컬에 저장
				tokenLocalDataSource.updateUserToken(
					userAccessToken = it.accessToken,
					userRefreshToken = it.refreshToken,
					userStatus = it.status,
					userProvider = null,
					onError = onError,
				)
				Timber.d("refreshToken: 토큰 갱신 성공 - ${it.accessToken}, ${it.refreshToken}, ${it.status}")
			}.catch {
				Timber.e("알 수 없는 오류")
				onError(it)
			}.collect()
		} ?: run {
			Timber.e("리프레시 토큰이 로컬에 없습니다")
			onError(Throwable("리프레시 토큰이 없습니다. 다시 로그인 해주세요."))
		}
	}

	override suspend fun clearLocalAuthCode(onError: suspend (Throwable) -> Unit) {
		authLocalDataSource.clearAuthCode(onError = onError)
	}

	override fun logoutRemoteAccount() {
		CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
			tokenRemoteDataSource.logoutAccount(
				// 해당 함수 호출부 다음 코드 라인의 Main Thread에서 접근하여 비우는 로직보다 먼저 접근
				accessToken = tokenLocalDataSource.getUserAccessToken({
					Timber.e("서버에 로그아웃 요청 실패: $it")
				}).firstOrNull() ?: "",
				onError = { Timber.e("서버에 로그아웃 요청 실패: $it") },
			)
		}
	}
}
