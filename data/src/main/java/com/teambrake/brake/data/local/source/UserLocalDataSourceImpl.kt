package com.teambrake.brake.data.local.source

import androidx.datastore.core.DataStore
import com.teambrake.brake.core.datastore.model.DatastoreOnboarding
import com.teambrake.brake.core.datastore.model.DatastoreUserInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

internal class UserLocalDataSourceImpl @Inject constructor(
	private val userInfoDataStore: DataStore<DatastoreUserInfo>,
	private val onboardingDataStore: DataStore<DatastoreOnboarding>,
) : UserLocalDataSource {
	override suspend fun updateOnboardingFlag(
		isComplete: Boolean,
		onError: suspend (Throwable) -> Unit,
	) {
		onboardingDataStore.updateData {
			it.copy(flag = isComplete)
		}
		val result = onboardingDataStore.data.firstOrNull()?.flag == true
		Timber.e("Onboarding flag updated: $result")
	}

	override fun getOnboardingFlag(onError: suspend (Throwable) -> Unit): Flow<Boolean> = flow {
		onboardingDataStore.data
			.catch {
				onError(Throwable("온보딩 플래그를 가져오는데 실패했습니다"))
			}.collect { onboardingFlag ->
				emit(onboardingFlag.flag)
			}
	}

	override suspend fun clearUserInfo(onError: suspend (Throwable) -> Unit) {
		userInfoDataStore.updateData {
			DatastoreUserInfo.Empty
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 정보 비우기에 실패했습니다"))
		}
	}

	override suspend fun updateNickname(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		userInfoDataStore.updateData {
			it.copy(nickname = nickname)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 닉네임 업데이트를 실패했습니다"))
		}
	}

	override suspend fun clearNickname(onError: suspend (Throwable) -> Unit) {
		userInfoDataStore.updateData {
			it.copy(nickname = null)
		}.runCatching {
			// 성공적인 업데이트 후 아무 작업도 하지 않음
		}.onFailure {
			onError(Throwable("유저 닉네임을 초기화하는데 실패했습니다"))
		}
	}

	override fun getNickname(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		userInfoDataStore.data
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
