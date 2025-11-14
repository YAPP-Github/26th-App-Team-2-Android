package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.StartOfflineModeUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named

class StartOfflineModeUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val sessionRepository: SessionRepository,
	@Named("TokenRepo") private val localRepository: TokenRepository,
) : StartOfflineModeUseCase {
	override suspend fun invoke(offlineNickname: String, onError: suspend (Throwable) -> Unit): Destination {
		nicknameRepository.saveLocalUserName(
			nickname = offlineNickname,
			onError = onError,
		)
		localRepository.setOfflineModeStatus(
			onError = onError,
		)
		return sessionRepository.getOnboardingFlag(onError = onError).firstOrNull()
			?.let { isOnboardingCompleted ->
				if (isOnboardingCompleted) {
					Destination.PermissionOrHome
				} else {
					Destination.Onboarding
				}
			} ?: Destination.Onboarding
	}
}
