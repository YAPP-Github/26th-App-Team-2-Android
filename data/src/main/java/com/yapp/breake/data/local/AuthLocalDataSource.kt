package com.yapp.breake.data.local

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreAuthCode
import com.yapp.breake.core.model.user.exception.LocalException.DataStoreEmptyException
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

internal class AuthLocalDataSource @Inject constructor(
	private val authCodeDataStore: DataStore<DatastoreAuthCode>,
) {

	suspend fun updateAuthCode(authCode: String?, onError: suspend (Throwable) -> Unit) {
		authCodeDataStore.updateData { authCodeObj ->
			authCodeObj.copy(authCode = authCode)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("인증 코드를 업데이트하는데 실패했습니다"))
		}
	}

	fun getAuthCode(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		authCodeDataStore.data
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

	suspend fun clearAuthCode(onError: suspend (Throwable) -> Unit) {
		authCodeDataStore.updateData {
			it.copy(authCode = null)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("인증 코드를 초기화하는데 실패했습니다"))
		}
	}
}
