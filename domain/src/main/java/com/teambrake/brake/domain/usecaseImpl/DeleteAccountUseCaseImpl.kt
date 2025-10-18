package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.DeleteAccountUseCase
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
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
			sessionRepository.clearEntireDataStore(onError = { throwable ->
				onError(throwable)
				throw LocalException()
			})
			appGroupRepository.clearAppGroup()
			appRepository.clearApps()
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
