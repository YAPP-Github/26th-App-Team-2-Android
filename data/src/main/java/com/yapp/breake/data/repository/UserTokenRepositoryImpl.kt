package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.data.util.safeLocalCall
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

internal class UserTokenRepositoryImpl @Inject constructor(
	@Named("UserToken") private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : UserTokenRepository {

	override suspend fun updateUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
		userStatus: UserTokenStatus?,
	) {
		userTokenDataSource.updateData { tokenObject ->
			tokenObject.copy(
				accessToken = userAccessToken ?: tokenObject.accessToken,
				refreshToken = userRefreshToken ?: tokenObject.refreshToken,
				status = userStatus ?: tokenObject.status,
			)
		}
	}

	override fun getUserAccessToken(): Flow<ResponseResult<String>> =
		safeLocalCall(
			localCall = userTokenDataSource.data,
			mapper = { it.accessToken },
			predicateOnSuccess = { it.accessToken.isNotBlank() },
		)

	override fun getUserRefreshToken(): Flow<ResponseResult<String>> =
		safeLocalCall(
			localCall = userTokenDataSource.data,
			mapper = { it.refreshToken },
			predicateOnSuccess = { it.refreshToken.isNotBlank() },
		)

	override fun getUserStatus(): Flow<ResponseResult<UserTokenStatus>> =
		safeLocalCall(
			localCall = userTokenDataSource.data,
			mapper = { it.status },
			predicateOnSuccess = {
				it.accessToken.isNotBlank() &&
					it.refreshToken.isNotBlank() &&
					it.status != UserTokenStatus.INACTIVE
			},
		)
}
