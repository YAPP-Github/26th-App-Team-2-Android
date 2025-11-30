package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.StartOfflineModeUseCaseError
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.StartOfflineModeUseCase
import javax.inject.Inject
import javax.inject.Named

class StartOfflineModeUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val sessionRepository: SessionRepository,
	@Named("TokenRepo") private val localRepository: TokenRepository,
) : StartOfflineModeUseCase {
	override suspend fun invoke(offlineNickname: String): BrakeResult<Destination, StartOfflineModeUseCaseError> {
		localRepository.setOfflineModeStatus(
			onError = {},
		)
		nicknameRepository.saveLocalUserName(
			nickname = offlineNickname,
			onError = {},
		)

		return when (val result = sessionRepository.getOnboardingFlag()) {
			is BrakeResult.Success -> {
				val isOnboardingCompleted = result.data
				val destination = if (isOnboardingCompleted) {
					Destination.PermissionOrHome
				} else {
					Destination.Onboarding
				}
				BrakeResult.Success(destination)
			}

			is BrakeResult.Error -> {
				BrakeResult.Error(result.error)
			}
		}
	}
}
