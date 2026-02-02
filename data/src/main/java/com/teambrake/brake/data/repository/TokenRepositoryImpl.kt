package com.teambrake.brake.data.repository

import com.teambrake.brake.core.auth.google.GoogleAuthManager
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.core.model.user.UserToken
import com.teambrake.brake.data.local.source.AuthLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.TokenRemoteDataSource
import com.teambrake.brake.data.repository.base.BaseRepository
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.domain.repository.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val tokenRemoteDataSource: TokenRemoteDataSource,
	private val authLocalDataSource: AuthLocalDataSource,
	private val googleAuthManager: GoogleAuthManager,
) : BaseRepository(tokenLocalDataSource),
	TokenRepository {

	override suspend fun setOfflineModeStatus(): Result<Unit> = try {
		tokenLocalDataSource.updateUserToken(
			userAccessToken = null,
			userRefreshToken = null,
			userStatus = UserStatus.OFFLINE,
			onError = { throw it },
		)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override fun getUserStatus(): Flow<UserStatus> =
		tokenLocalDataSource.getUserStatus(onError = {})

	override fun getRemoteTokens(
		provider: String,
		authorizationCode: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		emitAll(
			executeFlowWithStatusCheck(
				flowProvider = {
					tokenRemoteDataSource.getTokens(
						provider = provider,
						authorizationCode = authorizationCode,
						onError = onError,
					)
				},
				offlineFlowProvider = {
					flow {
						val error = Exception("오프라인 모드에서 토큰 획득 불가")
						Timber.e("오프라인 모드에서 토큰 획득 시도")
						onError(error)
						throw error
					}
				},
			).map {
				// TODO: authCode 습득 후에 WorkManager 를 사용하여 AuthCode 삭제 작업 예약
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
			}.catch { e ->
				Timber.e("토큰 갱신 실패: $e")
				onError(e)
			},
		)
	}

	override fun getRemoteTokensRetry(
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserToken> = flow {
		executeFlowWithStatusCheck(
			flowProvider = {
				authLocalDataSource.getAuthCode(onError = onError)
			},
			offlineFlowProvider = {
				flow {
					val error = Exception("오프라인 모드에서 AuthCode 획득 불가")
					Timber.e("오프라인 모드에서 AuthCode 획득 불가")
					onError(error)
					throw error
				}
			},
		).collect { authCode ->
			getRemoteTokens(
				provider = provider,
				authorizationCode = authCode,
				onError = onError,
			).catch { e ->
				Timber.e("토큰 재시도 실패: $e")
				onError(e)
			}.collect { token ->
				emit(token)
			}
		}
	}

	override suspend fun clearLocalTokens(): Result<Unit> = try {
		authLocalDataSource.updateAuthCode(
			authCode = null,
			onError = { throw it },
		)
		tokenLocalDataSource.updateUserToken(
			userAccessToken = null,
			userRefreshToken = null,
			userStatus = UserStatus.INACTIVE,
			onError = { throw it },
		)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun refreshTokens(): Result<Unit> = try {
		val refreshToken = tokenLocalDataSource.getUserRefreshToken { throw it }.firstOrNull()
		refreshToken?.let {
			executeFlowWithStatusCheck(
				flowProvider = {
					tokenRemoteDataSource.refreshTokens(
						refreshToken = refreshToken,
						onError = { throw it },
					)
				},
				offlineFlowProvider = {
					flow {
						val error = Exception("오프라인 모드에서 토큰 갱신 불가")
						Timber.e("오프라인 모드에서 토큰 갱신 불가")
						throw error
					}
				},
			).map {
				it.toData()
			}.onEach {
				// 토큰 갱신 성공 시 로컬에 저장
				tokenLocalDataSource.updateUserToken(
					userAccessToken = it.accessToken,
					userRefreshToken = it.refreshToken,
					userStatus = it.status,
					onError = { throw it },
				)
				Timber.d("refreshToken: 토큰 갱신 성공 - ${it.accessToken}, ${it.refreshToken}, ${it.status}")
			}.catch { e ->
				Timber.e("토큰 갱신 실패: ${e.message}")
				throw e
			}.collect()
			Result.success(Unit)
		} ?: run {
			Timber.e("리프레시 토큰이 로컬에 없습니다")
			Result.failure(Throwable("리프레시 토큰이 없습니다. 다시 로그인 해주세요."))
		}
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun clearLocalAuthCode(): Result<Unit> = try {
		authLocalDataSource.clearAuthCode(onError = { throw it })
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun logoutRemoteAccount(): Result<Unit> = try {
		executeWithStatusCheck(
			runIfOnline = {
				tokenRemoteDataSource.logoutAccount(
					// 해당 함수 호출부 다음 코드 라인의 Main Thread에서 접근하여 비우는 로직보다 먼저 접근
					accessToken = tokenLocalDataSource.getUserAccessToken {
						Timber.e("서버에 로그아웃 요청 실패: $it")
					}.firstOrNull() ?: "",
					onError = { throw it },
				)
				googleAuthManager.signOutGoogleAuth()
			},
			runIfOffline = {
				// 오프라인 모드에서는 로그아웃 스킵
				Timber.d("오프라인 모드: 서버에 로그아웃 요청 스킵")
			},
		)
		Result.success(Unit)
	} catch (e: Exception) {
		Timber.e("서버에 로그아웃 요청 실패: ${e.message}")
		Result.failure(e)
	}
}
