package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
import com.yapp.breake.domain.repository.SessionRepository
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.usecase.DeleteAccountUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteAccountUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DeleteAccountUseCase {
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Destination {
		return try {
			sessionRepository.clearRemoteAccount(
				onError = { throwable ->
					onError(throwable)
					throw ServerException()
				},
			)
			tokenRepository.clearLocalAuthCode(onError = onError)
			sessionRepository.clearEntireDataStore(onError = onError)
			appGroupRepository.clearAppGroup()
			appRepository.clearApps()
			Destination.Login
		} catch (_: ServerException) {
			Destination.NotChanged
		}
	}

	companion object {
		class ServerException : Exception()
	}
}
