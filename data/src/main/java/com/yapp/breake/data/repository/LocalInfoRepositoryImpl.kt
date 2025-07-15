package com.yapp.breake.data.repository

import androidx.datastore.core.DataStore
import com.yapp.breake.core.datastore.model.DatastoreUserInfo
import com.yapp.breake.domain.repository.LocalInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class LocalInfoRepositoryImpl @Inject constructor(
	private val userInfoDataSource: DataStore<DatastoreUserInfo>,
) : LocalInfoRepository {
	override suspend fun updateNickname(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		userInfoDataSource.updateData {
			it.copy(nickname = nickname)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 닉네임 업데이트를 실패했습니다"))
		}
	}

	override fun getNickname(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		userInfoDataSource.data
			.catch {
				onError(Throwable("유저 닉네임을 가져오는데 실패했습니다"))
			}
			.collect { userInfo ->
				userInfo.nickname?.let {
					emit(userInfo.nickname!!)
				} ?: run {
					onError(Throwable("닉네임이 설정되어 있지 않습니다"))
				}
			}
	}

}
