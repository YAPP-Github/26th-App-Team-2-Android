package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
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
		// 1. 원격 사용자 정보 가져오기
		val userName = nicknameRepository.getRemoteUserName { e ->
			// 오프라인 상태 처리
			throw NetworkException(e.message ?: "Network error")
		}.runCatching {
			firstOrNull() ?: throw Exception("Failed to fetch user name")
		}.getOrElse { exception ->
			throw exception
		}

		// 2. 로컬 저장
		nicknameRepository.saveLocalUserName(
			userName.nickname,
			onError = { e ->
				throw LocalStorageException(e.message ?: "Local storage error")
			},
		)

		// 3. 온보딩 상태 확인
		getDestinationByOnboardingStatus()

	} catch (_: NetworkException) {
		val userStatus = tokenRepository.getUserStatus { e ->
			throw LocalStorageException(e.message ?: "Local storage error")
		}
		if (userStatus == UserStatus.OFFLINE) {
			getDestinationByOnboardingStatus()
		} else {
			Destination.Login.also { clearAllData() }
		}
	} catch (_: LocalStorageException) {
		Destination.Login.also { clearAllData() }
	} catch (_: Exception) {
		Destination.Login.also { clearAllData() }
	}

	private suspend fun getDestinationByOnboardingStatus(): Destination {
		val isCompleted = sessionRepository.getOnboardingFlag { e ->
			throw LocalStorageException(e.message ?: "Local storage error")
		}.firstOrNull() == true

		return if (isCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	}

	private suspend fun clearAllData() {
		appGroupRepository.clearAppGroup()
		appRepository.clearApps()
	}

	// TODO: 간단하게 흐름을 처리하기 위해 예외 처리로 구현. 그러나 분기 처리(Result)와 예외 처리(Exception)의 구분이 필요하므로 추후 리팩토링 필요
	companion object {
		class LocalStorageException(override val message: String) : Exception()
		class NetworkException(override val message: String) : Exception()
	}
}
