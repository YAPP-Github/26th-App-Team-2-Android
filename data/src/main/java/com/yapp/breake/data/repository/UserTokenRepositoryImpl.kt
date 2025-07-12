package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

internal class UserTokenRepositoryImpl @Inject constructor(
	@Named("UserToken") private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : UserTokenRepository {

	override suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserTokenStatus?,
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
				if (tokenData.accessToken.isNotBlank()) {
					emit(tokenData.accessToken)
				}
				// 조건에 맞지 않으면 데이터 전송 안함
			}
	}

	override fun getUserRefreshToken(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		userTokenDataSource.data
			.catch {
				onError(Throwable("유저 인증을 가져오는데 실패했습니다"))
			}
			.collect { tokenData ->
				if (tokenData.refreshToken.isNotBlank()) {
					emit(tokenData.refreshToken)
				}
				// 조건에 맞지 않으면 데이터 전송 안함
			}
	}

	override fun getUserStatus(onError: suspend (Throwable) -> Unit): Flow<UserTokenStatus> = flow {
		userTokenDataSource.data
			.catch {
				onError(it)
			}
			.collect { tokenData ->
				emit(tokenData.status)
			}
	}
}
