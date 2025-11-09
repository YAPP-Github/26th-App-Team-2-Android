package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DecideStartDestinationUseCase {

	override suspend fun invoke(): Destination = try {
		val userName = nicknameRepository.getRemoteUserName(
			onError = {},
		).first()
		nicknameRepository.saveLocalUserName(
			nickname = userName.nickname,
			onError = {},
		)
		val isOnboardingCompleted = runBlocking {
			sessionRepository.getOnboardingFlag(
				onError = { throw LocalStorageException() },
			).firstOrNull() == true
		}
		if (isOnboardingCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	} catch (_: LocalStorageException) {
		Destination.Onboarding
	} catch (_: Exception) {
		appGroupRepository.clearAppGroup()
		appRepository.clearApps()
		Destination.Login
	}

	companion object {
		class LocalStorageException : Exception()
	}
}
