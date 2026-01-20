package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LogoutUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.LogoutUseCase
import javax.inject.Inject
import javax.inject.Named

class LogoutUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	private val nicknameRepository: NicknameRepository,
	private val authRepository: AuthRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : LogoutUseCase {
	override suspend fun invoke(): BrakeResult<Destination, LogoutUseCaseError> {
		tokenRepository.logoutRemoteAccount()
		return when (val localResult = authRepository.clearAuthDataStore()) {
			is BrakeResult.Success -> {
				nicknameRepository.clearLocalName { }
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
