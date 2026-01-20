package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.NameLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.repository.mapper.toData
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.RemoteServerNotReachedError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.AuthStatusSuccess
import com.teambrake.brake.domain.model.result.success.OfflineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.OnlineAuthorizedSuccess
import com.teambrake.brake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class NicknameRepositoryImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val nameRemoteDataSource: NameRemoteDataSource,
	private val nameLocalDataSource: NameLocalDataSource,
) : NicknameRepository {

	override suspend fun getRemoteUserName(): BrakeResult<AuthStatusSuccess<UserName>, ApiCallError> {
		return try {
			if (checkOfflineMode()) {
				return BrakeResult.Success(OfflineAuthorizedSuccess(UserName("", UserStatus.OFFLINE)))
			}

			val userName = nameRemoteDataSource.getUserName { e ->
				throw Exception(e)
			}.map {
				it.toData()
			}.firstOrNull()

			if (userName != null) {
				BrakeResult.Success(OnlineAuthorizedSuccess(userName))
			} else {
				BrakeResult.Error(RemoteServerNotReachedError)
			}
		} catch (e: Exception) {
			BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	override fun getLocalUserName(onError: suspend (Throwable) -> Unit): Flow<String> = flow {
		nameLocalDataSource.getNickname().catch {
			onError(Throwable("failed to get local nickname"))
		}.collect {
			emit(it)
		}
	}

	override suspend fun saveLocalUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	) {
		val result = nameLocalDataSource.updateNickname(nickname = nickname)
		if (!result) {
			onError(Throwable("failed to save nickname locally"))
		}
	}

	override suspend fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): BrakeResult<AuthStatusSuccess<UserName>, ApiCallError> {
		return try {
			if (checkOfflineMode()) {
				return BrakeResult.Success(
					OfflineAuthorizedSuccess(
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
			).map {
				it.toData()
			}.firstOrNull()

			userName?.let {
				val localSaveResult = nameLocalDataSource.updateNickname(nickname)
				if (!localSaveResult) {
					onError(Throwable("failed to save nickname locally"))
				}
			}

			if (userName != null) {
				BrakeResult.Success(OnlineAuthorizedSuccess(userName))
			} else {
				BrakeResult.Error(RemoteServerNotReachedError)
			}
		} catch (e: Exception) {
			BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	override suspend fun clearLocalName(onError: suspend (Throwable) -> Unit) {
		val result = nameLocalDataSource.clearNickname()
		if (!result) {
			onError(Throwable("failed to clear local nickname"))
		}
	}

	private suspend fun checkOfflineMode(): Boolean = tokenLocalDataSource.getUserStatus(
		onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
	).firstOrNull()?.run {
		this == UserStatus.OFFLINE
	} ?: false
}
