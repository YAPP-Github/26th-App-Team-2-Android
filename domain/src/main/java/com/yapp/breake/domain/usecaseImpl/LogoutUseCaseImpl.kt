package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.repository.SessionRepository
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.usecase.LogoutUseCase
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
