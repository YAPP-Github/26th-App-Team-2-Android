package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.local.source.UserLocalDataSource
import com.teambrake.brake.data.remote.source.AccountRemoteDataSource
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.repository.SessionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

internal class SessionRepositoryImpl @Inject constructor(
	private val userLocalDataSource: UserLocalDataSource,
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val accountRemoteDataSource: AccountRemoteDataSource,
) : SessionRepository {

	override suspend fun updateLocalOnboardingFlag(isComplete: Boolean): BrakeResult<Unit, ApiCallError> =
		try {
			userLocalDataSource.updateOnboardingFlag(
				isComplete = isComplete,
				onError = { throw Exception(it.message) },
			)
			BrakeResult.Success(Unit)
		} catch (e: Exception) {
			BrakeResult.Error(LocalApiCallError(e))
		}

	override suspend fun getOnboardingFlag(): BrakeResult<Boolean, LocalApiCallError> = try {
		val isComplete = userLocalDataSource.getOnboardingFlag(
			onError = { throw Exception(it) },
		).first()

		BrakeResult.Success(isComplete)
	} catch (e: Exception) {
		BrakeResult.Error(LocalApiCallError(e))
	}

	override suspend fun clearEntireDataStore(): BrakeResult<Unit, ApiCallError> = try {
		userLocalDataSource.clearUserInfo(
			onError = {
				throw Exception(it)
			},
		)
		tokenLocalDataSource.clearUserToken(
			onError = {
				throw Exception(it)
			},
		)
		BrakeResult.Success(Unit)
	} catch (e: Exception) {
		Timber.e(e, "Error clearing data store")
		BrakeResult.Error(LocalApiCallError(e))
	}

	override suspend fun clearRemoteAccount(): BrakeResult<Unit, ApiCallError> = try {
		val isOffline = checkOfflineMode()
		if (!isOffline) {
			accountRemoteDataSource.deleteAccount(
				onError = {
					throw Exception(it)
				},
			)
		}
		BrakeResult.Success(Unit)
	} catch (e: Exception) {
		BrakeResult.Error(LocalApiCallError(e))
	}

	private suspend fun checkOfflineMode(): Boolean = tokenLocalDataSource.getUserStatus(
		onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
	).firstOrNull()?.run {
		this == UserStatus.OFFLINE
	} ?: false
}
