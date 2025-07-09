package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.data.model.LocalException.DataEmptyException
import com.yapp.breake.data.util.safeLocalCall
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

internal class UserTokenRepositoryImpl @Inject constructor(
	@Named("UserToken") private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : UserTokenRepository {

	override suspend fun saveUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserTokenStatus?,
	): Flow<Unit> = flow<Unit> {
		safeLocalCall {
			userTokenDataSource.updateData {
				it.copy(
					accessToken = userAccessToken ?: it.accessToken,
					refreshToken = userRefreshToken ?: it.refreshToken,
					status = userStatus ?: it.status,
				)
			}
		}
	}

	override suspend fun getUserAccessToken(): Flow<String> = safeLocalCall {
		userTokenDataSource.data.map {
			it.accessToken.isBlank().let { isBlank ->
				if (isBlank) {
					throw DataEmptyException()
				} else {
					it.accessToken
				}
			}
		}
	}

	override suspend fun getUserRefreshToken(): Flow<String> = safeLocalCall {
		userTokenDataSource.data.map {
			it.refreshToken.isBlank().let { isBlank ->
				if (isBlank) {
					throw DataEmptyException()
				} else {
					it.refreshToken
				}
			}
		}
	}

	override suspend fun getUserStatus(): Flow<UserTokenStatus> = safeLocalCall {
		userTokenDataSource.data.map {
			if (it.accessToken.isBlank() ||
				it.refreshToken.isBlank() ||
				it.status == UserTokenStatus.INACTIVE
			) {
				UserTokenStatus.INACTIVE
			} else {
				it.status
			}
		}
	}
}
