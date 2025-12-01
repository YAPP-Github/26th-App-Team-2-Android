package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LogoutUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.LogoutUseCase
import javax.inject.Inject
import javax.inject.Named

class LogoutUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	private val sessionRepository: SessionRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : LogoutUseCase {
	override suspend fun invoke(): BrakeResult<Destination, LogoutUseCaseError> {
		tokenRepository.logoutRemoteAccount()
		return when (val localResult = sessionRepository.clearEntireDataStore()) {
			is BrakeResult.Success -> {
				appGroupRepository.clearAppGroup()
				appRepository.clearApps()
				BrakeResult.Success(Destination.Login)
			}

			is BrakeResult.Error -> {
				BrakeResult.Error(localResult.error)
			}
		}
	}
}
