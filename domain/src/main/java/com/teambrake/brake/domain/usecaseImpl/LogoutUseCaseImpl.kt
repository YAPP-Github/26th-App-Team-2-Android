package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
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
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Destination {
		return try {
			tokenRepository.logoutRemoteAccount()
			sessionRepository.clearEntireDataStore(
				onError = { throwable ->
					onError(throwable)
					throw LocalException()
				},
			)
			appGroupRepository.clearAppGroup()
			appRepository.clearApps()
			Destination.Login
		} catch (_: LocalException) {
			Destination.PermissionOrHome
		}
	}

	companion object {
		class LocalException : Exception()
	}
}
