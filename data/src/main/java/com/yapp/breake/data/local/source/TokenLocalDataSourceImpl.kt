package com.yapp.breake.data.local.source

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class TokenLocalDataSourceImpl @Inject constructor(
	private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : TokenLocalDataSource {
	override suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserStatus?,
		userProvider: String?,
		onError: suspend (Throwable) -> Unit,
	) {
		userTokenDataSource.updateData { tokenObject ->
			tokenObject.copy(
				accessToken = userAccessToken ?: tokenObject.accessToken,
				refreshToken = userRefreshToken ?: tokenObject.refreshToken,
				status = userStatus ?: tokenObject.status,
				provider = userProvider ?: tokenObject.provider,
			)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 인증 업데이트를 실패했습니다"))
		}
	}

	override fun getUserAccessToken(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		userTokenDataSource.data
			.catch {
				onError(Throwable("유저 인증을 가져오는데 실패했습니다"))
			}
			.collect { tokenData ->
				tokenData.accessToken?.let {
					emit(it)
				} ?: run {
					onError(Throwable("유저 인증 액세스 토큰이 설정되어 있지 않습니다"))
				}
			}
	}

	override fun getUserRefreshToken(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		userTokenDataSource.data
			.catch {
				onError(Throwable("유저 인증을 가져오는데 실패했습니다"))
			}
			.collect { tokenData ->
				tokenData.refreshToken?.let {
					emit(it)
				} ?: run {
					onError(Throwable("유저 인증 리프레시 토큰이 설정되어 있지 않습니다"))
				}
			}
	}

	override fun getUserStatus(onError: suspend (Throwable) -> Unit): Flow<UserStatus> = flow {
		userTokenDataSource.data
			.catch {
				onError(it)
			}
			.collect { tokenData ->
				emit(tokenData.status)
			}
	}

	override fun getUserProvider(onError: suspend (Throwable) -> Unit): Flow<String?> = flow {
		userTokenDataSource.data
			.catch {
				onError(it)
			}
			.collect { tokenData ->
				emit(tokenData.provider)
			}
	}

	override suspend fun clearUserToken(onError: suspend (Throwable) -> Unit) {
		userTokenDataSource.updateData { tokenObject ->
			tokenObject.copy(
				accessToken = null,
				refreshToken = null,
				status = UserStatus.INACTIVE,
				provider = null,
			)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 인증을 초기화하는데 실패했습니다"))
		}
	}
}
