package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreAuthCode
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.model.user.exception.LocalException.DataStoreEmptyException
import com.yapp.breake.domain.repository.LocalTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LocalTokenRepositoryImpl @Inject constructor(
	private val userTokenDataSource: DataStore<DatastoreUserToken>,
	private val authCodeDataSource: DataStore<DatastoreAuthCode>,
) : LocalTokenRepository {

	override suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserStatus?,
		onError: suspend (Throwable) -> Unit,
	) {
		userTokenDataSource.updateData { tokenObject ->
			tokenObject.copy(
				accessToken = userAccessToken ?: tokenObject.accessToken,
				refreshToken = userRefreshToken ?: tokenObject.refreshToken,
				status = userStatus ?: tokenObject.status,
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

	override suspend fun updateAuthCode(authCode: String?, onError: suspend (Throwable) -> Unit) {
		authCodeDataSource.updateData { authCodeObj ->
			authCodeObj.copy(authCode = authCode)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("인증 코드를 업데이트하는데 실패했습니다"))
		}
	}

	override fun getAuthCode(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		authCodeDataSource.data
			.catch {
				onError(it)
			}
			.collect {
				it.authCode?.let {
					emit(it)
				} ?: run {
					onError(DataStoreEmptyException("인증 코드가 만료되었습니다"))
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
}
