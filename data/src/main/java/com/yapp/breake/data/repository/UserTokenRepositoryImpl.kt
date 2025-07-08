package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserToken
import com.yapp.breake.data.model.LocalException.DataEmptyException
import com.yapp.breake.domain.repository.UserTokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserTokenRepositoryImpl @Inject constructor(
	private val userTokenDataSource: DataStore<DatastoreUserToken>,
) : UserTokenRepository {

	override suspend fun saveUserToken(
		userAccessToken: String?,
		userRefreshToken: String?,
	): Flow<Unit> = flow<Unit> {
		userTokenDataSource.updateData {
			it.copy(
				accessToken = userAccessToken ?: it.accessToken,
				refreshToken = userRefreshToken ?: it.refreshToken,
			)
		}
	}

	override suspend fun getUserAccessToken(): Flow<String> = userTokenDataSource.data.map {
		it.accessToken.isBlank().let { isBlank ->
			if (isBlank) {
				throw DataEmptyException()
			} else {
				it.accessToken
			}
		}
	}

	override suspend fun getUserRefreshToken(): Flow<String> = userTokenDataSource.data.map {
		it.refreshToken.isBlank().let { isBlank ->
			if (isBlank) {
				throw DataEmptyException()
			} else {
				it.refreshToken
			}
		}
	}
}
