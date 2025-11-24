package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.local.source.UserLocalDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.RemoteServerNotReachedError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.ModeSuccess
import com.teambrake.brake.domain.model.result.success.OfflineModeSuccess
import com.teambrake.brake.domain.model.result.success.OnlineModeSuccess
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val userLocalDataSource: UserLocalDataSource,
) : NicknameRepository {

	override suspend fun getRemoteUserName(): BrakeResult<ModeSuccess<UserName>, ApiCallError> {
		return try {
			if (checkOfflineMode()) {
				return BrakeResult.Success(OfflineModeSuccess(UserName("", UserStatus.OFFLINE)))
			}

			val userName = nameRemoteDataSource.getUserName { e ->
				throw Exception(e)
			}.map {
				it.toData()
			}.firstOrNull()

			if (userName != null) {
				BrakeResult.Success(OnlineModeSuccess(userName))
			} else {
				BrakeResult.Error(RemoteServerNotReachedError)
			}
		} catch (e: Exception) {
			BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	override fun getLocalUserName(onError: suspend (Throwable) -> Unit): Flow<String> =
		userLocalDataSource.getNickname(onError = onError)

	override suspend fun saveLocalUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		userLocalDataSource.updateNickname(
			nickname = nickname,
			onError = onError,
		)
	}

	override suspend fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): BrakeResult<ModeSuccess<UserName>, ApiCallError> {
		return try {
			if (checkOfflineMode()) {
				return BrakeResult.Success(
					OfflineModeSuccess(
						UserName(
							nickname = nickname,
							state = UserStatus.OFFLINE,
						),
					),
				)
			}

			val userName = nameRemoteDataSource.updateUserName(
				nickname = nickname,
				onError = onError,
			).onEach {
				// 새로운 닉네임을 로컬에 저장
				userLocalDataSource.updateNickname(nickname, onError = onError)
			}.map {
				it.toData()
			}.firstOrNull()

			if (userName != null) {
				BrakeResult.Success(OnlineModeSuccess(userName))
			} else {
				BrakeResult.Error(RemoteServerNotReachedError)
			}
		} catch (e: Exception) {
			BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		userLocalDataSource.clearNickname(onError = onError)
	}

	private suspend fun checkOfflineMode(): Boolean = tokenLocalDataSource.getUserStatus(
		onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
	).firstOrNull()?.run {
		this == UserStatus.OFFLINE
	} ?: false
}
