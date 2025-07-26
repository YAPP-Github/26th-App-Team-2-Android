package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.SessionRepository
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
) : DeleteAccountUseCase {
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Destination {
		return try {
			sessionRepository.clearRemoteAccount(
				onError = { throwable ->
					onError(throwable)
					throw ServerException()
				},
			)
			sessionRepository.clearEntireDataStore(onError = { throwable ->
				onError(throwable)
				throw LocalException()
			})
			Destination.Login
		} catch (_: Exception) {
			Destination.NotChanged
		}
	}

	companion object {
		class ServerException : Exception()
		class LocalException : Exception()
	}
}
