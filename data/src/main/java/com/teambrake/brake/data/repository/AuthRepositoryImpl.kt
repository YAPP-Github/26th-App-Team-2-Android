package com.teambrake.brake.data.repository

import com.teambrake.brake.data.local.source.OnboardingLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.remote.source.AccountRemoteDataSource
import com.teambrake.brake.data.repository.base.BaseRepository
import com.teambrake.brake.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

internal class AuthRepositoryImpl @Inject constructor(
	private val tokenLocalDataSource: TokenLocalDataSource,
	private val onboardingLocalDataSource: OnboardingLocalDataSource,
	private val accountRemoteDataSource: AccountRemoteDataSource,
) : BaseRepository(tokenLocalDataSource),
	AuthRepository {

	override suspend fun updateOnboardingFlag(isComplete: Boolean): Result<Unit> = try {
		val result = onboardingLocalDataSource.updateOnboardingFlag(isComplete = isComplete)
		if (result) {
			Result.success(Unit)
		} else {
			Result.failure(Exception("failed to update onboarding flag locally"))
		}
	} catch (e: Exception) {
		Result.failure(e)
	}

	override fun getOnboardingFlag(): Flow<Boolean> =
		onboardingLocalDataSource.getOnboardingFlag()

	override suspend fun clearAuthDataStore(): Result<Unit> = try {
		tokenLocalDataSource.clearUserToken(
			onError = {
				throw Exception(it)
			},
		)
		Result.success(Unit)
	} catch (e: Exception) {
		Timber.e(e, "Error clearing data store")
		Result.failure(e)
	}

	override suspend fun clearRemoteAccount(): Result<Unit> = try {
		if (isOnlineStatus()) {
			accountRemoteDataSource.deleteAccount(
				onError = {
					throw Exception(it)
				},
			)
			Result.success(Unit)
		} else {
			// 오프라인 모드에서는 원격 계정 삭제를 건너뜀
			Timber.d("오프라인 모드: 원격 계정 삭제 스킵")
			Result.success(Unit)
		}
	} catch (e: Exception) {
		Timber.e(e, "계정 삭제 중 오류 발생")
		Result.failure(e)
	}
}
