package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.SessionRepository
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
) : LogoutUseCase {
	override suspend fun invoke(onError: suspend (Throwable) -> Unit): Destination {
		return try {
			sessionRepository.clearEntireDataStore(
				onError = { throwable ->
					onError(throwable)
					throw LocalException()
				},
			)
			Destination.Login
		} catch (_: LocalException) {
			Destination.PermissionOrHome
		}
	}

	companion object {
		class LocalException : Exception()
	}
}
