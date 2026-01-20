package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.local.source.OnboardingLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.AccountRemoteDataSource
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.ApiCallError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
	private val onboardingLocalDataSource: OnboardingLocalDataSource,
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val accountRemoteDataSource: AccountRemoteDataSource,
) : AuthRepository {

	override suspend fun updateLocalOnboardingFlag(isComplete: Boolean): BrakeResult<Unit, ApiCallError> {
		val result = onboardingLocalDataSource.updateOnboardingFlag(isComplete = isComplete)
		return if (result) {
			BrakeResult.Success(Unit)
		} else {
			BrakeResult.Error(LocalApiCallError(Exception("Failed to update onboarding flag")))
		}
	}

	override suspend fun getOnboardingFlag(): BrakeResult<Boolean, LocalApiCallError> = try {
		val flag = onboardingLocalDataSource.getOnboardingFlag().first()
		BrakeResult.Success(flag)
	} catch (e: Exception) {
		Timber.e(e, "Error getting onboarding flag")
		BrakeResult.Error(LocalApiCallError(e))
	}

	override suspend fun clearAuthDataStore(): BrakeResult<Unit, ApiCallError> = try {
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
		val isOffline = shouldFetchRemote()
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

	private suspend fun shouldFetchRemote(): Boolean = tokenLocalDataSource.getUserStatus(
		onError = { /* 상태를 가져오는 중 오류가 발생해도 무시 */ },
	).firstOrNull()?.run {
		this == UserStatus.OFFLINE
	} ?: false
}
