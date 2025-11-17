package com.teambrake.brake.data.repository

import com.teambrake.brake.core.auth.google.GoogleAuthManager
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.core.model.user.UserToken
import com.teambrake.brake.data.local.source.AuthLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.TokenRemoteDataSource
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.data.repository.util.OfflineBlocker
import com.teambrake.brake.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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
	private val offlineBlocker: OfflineBlocker,
	private val tokenRemoteDataSource: TokenRemoteDataSource,
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val authLocalDataSource: AuthLocalDataSource,
	private val googleAuthManager: GoogleAuthManager,
) : TokenRepository {

	override suspend fun setOfflineModeStatus(
		onError: suspend (Throwable) -> Unit,
	) {
		tokenLocalDataSource.updateUserToken(
			userAccessToken = null,
			userRefreshToken = null,
			userStatus = UserStatus.OFFLINE,
			onError = onError,
		)
	}

	override suspend fun getUserStatus(onError: suspend (Throwable) -> Unit): UserStatus =
		tokenLocalDataSource.getUserStatus(onError).firstOrNull()
			?: UserStatus.INACTIVE

	override fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		offlineBlocker.blockFlow {
			tokenRemoteDataSource.getTokens(
				provider = provider,
				authorizationCode = authorizationCode,
				onError = onError,
			)
		}.map {
			it.toData()
		}.onEach {
			// authCode 습득 성공 시 토큰과 유저 상태 (회원, 비회원) 저장
			Timber.d("accessToken: ${it.accessToken}, refreshToken: ${it.refreshToken}, status: ${it.status}")
			tokenLocalDataSource.updateUserToken(
				userAccessToken = it.accessToken,
				userRefreshToken = it.refreshToken,
				userStatus = it.status,
				onError = onError,
			)
		}.catch {
			Timber.e("토큰 갱신 실패: $it")
			onError(it)
		}.collect { emit(it) }
	}

	override fun getRemoteTokensRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		offlineBlocker.blockFlow {
			authLocalDataSource.getAuthCode(onError = onError)
		}.collect { authCode ->
			getRemoteTokens(
				provider = provider,
				authorizationCode = authCode,
				onError = onError,
			).catch {
				Timber.e("토큰 재시도 실패: $it")
				onError(it)
			}.collect { token ->
				emit(token)
			}
		}
	}

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

	override suspend fun refreshTokens(onError: suspend (Throwable) -> Unit) {
		val refreshToken = tokenLocalDataSource.getUserRefreshToken(onError).firstOrNull()
		refreshToken?.let {
			offlineBlocker.blockFlow {
				tokenRemoteDataSource.refreshTokens(
					refreshToken = refreshToken,
					onError = onError,
				)
			}.map {
				it.toData()
			}.onEach {
				// 토큰 갱신 성공 시 로컬에 저장
				tokenLocalDataSource.updateUserToken(
					userAccessToken = it.accessToken,
					userRefreshToken = it.refreshToken,
					userStatus = it.status,
					onError = onError,
				)
				Timber.d("refreshToken: 토큰 갱신 성공 - ${it.accessToken}, ${it.refreshToken}, ${it.status}")
			}.catch {
				Timber.e("토큰 갱신 실패: ${it.message}")
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
			try {
				offlineBlocker.block {
					tokenRemoteDataSource.logoutAccount(
						// 해당 함수 호출부 다음 코드 라인의 Main Thread에서 접근하여 비우는 로직보다 먼저 접근
						accessToken = tokenLocalDataSource.getUserAccessToken(
							{
								Timber.e("서버에 로그아웃 요청 실패: $it")
							},
						).firstOrNull() ?: "",
						onError = { Timber.e("서버에 로그아웃 요청 실패: $it") },
					)
					googleAuthManager.signOutGoogleAuth()
				}
			} catch (e: Exception) {
				Timber.e("서버에 로그아웃 요청 실패: ${e.message}")
			}
		}
	}
}
