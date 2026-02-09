package com.teambrake.brake.data.local.source

import androidx.datastore.core.DataStore
import com.teambrake.brake.core.datastore.model.DatastoreUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class NameLocalDataSourceImpl @Inject constructor(
	private val userInfoDataStore: DataStore<DatastoreUserInfo>,
) : NameLocalDataSource {
	override suspend fun updateNickname(nickname: String): Boolean = runCatching {
		userInfoDataStore.updateData {
			it.copy(nickname = nickname)
		}
	}.onFailure {
		Timber.e(it, "Error updating user nickname locally")
	}.isSuccess

	override suspend fun clearNickname(): Boolean = runCatching {
		userInfoDataStore.updateData {
			it.copy(nickname = null)
		}
	}.onFailure {
		Timber.e(it, "Error clearing user nickname locally")
	}.isSuccess

	override fun getNickname(): Flow<String> = flow {
		userInfoDataStore.data.catch {
			Timber.e(it, "Error fetching user nickname locally")
		}.collect { userInfo ->
			userInfo.nickname?.let {
				emit(it)
			} ?: error("User nickname is not set locally")
		}
	}
}
