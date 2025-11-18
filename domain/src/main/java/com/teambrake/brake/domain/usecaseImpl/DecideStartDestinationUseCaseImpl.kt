package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	private val sessionRepository: SessionRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DecideStartDestinationUseCase {

	override suspend fun invoke(): Destination = try {
		val userName = nicknameRepository.getRemoteUserName(
			onError = {},
		).runCatching {
			first()
		}.onFailure { exception ->
			// 추후 네트워크 예외 세분화 필요
			if (exception is Exception) {
				throw NetworkException()
			}
		}

		nicknameRepository.saveLocalUserName(
			nickname = userName.getOrThrow().nickname,
			onError = { throw LocalStorageException() },
		)
		val isOnboardingCompleted = sessionRepository.getOnboardingFlag(
			onError = { throw LocalStorageException() },
		).firstOrNull() == true

		if (isOnboardingCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	} catch (_: NetworkException) {
		// 오프라인 모드: 로컬 데이터로만 진행
		val isOfflineMode = tokenRepository.getUserStatus(
			onError = { throw LocalStorageException() },
		)
		if (isOfflineMode == UserStatus.OFFLINE) {
			val isOnboardingCompleted = sessionRepository.getOnboardingFlag(
				onError = { throw LocalStorageException() },
			).firstOrNull() == true

			if (isOnboardingCompleted) {
				Destination.PermissionOrHome
			} else {
				Destination.Onboarding
			}
		} else {
			appGroupRepository.clearAppGroup()
			appRepository.clearApps()
			Destination.Login
		}
	} catch (_: Exception) {
		appGroupRepository.clearAppGroup()
		appRepository.clearApps()
		Destination.Login
	}

	companion object {
		class LocalStorageException : Exception()
		class NetworkException : Exception()
	}
}
