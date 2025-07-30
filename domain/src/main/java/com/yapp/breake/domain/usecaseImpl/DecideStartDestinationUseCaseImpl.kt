package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.domain.repository.NicknameRepository
import com.yapp.breake.domain.repository.SessionRepository
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
) : DecideStartDestinationUseCase {

	override fun invoke(): Destination = try {
		runBlocking {
			tokenRepository.refreshTokens(
				onError = { throw ServerException() },
			)
			val userName = nicknameRepository.getRemoteUserName(
				onError = {},
			).first()
			nicknameRepository.saveLocalUserName(
				nickname = userName.nickname,
				onError = {},
			)
		}
		val isOnboardingCompleted = runBlocking {
			sessionRepository.getOnboardingFlag(
				onError = { throw LocalStorageException() },
			).firstOrNull() == true
		}
		if (isOnboardingCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	} catch (_: LocalStorageException) {
		Destination.Onboarding
	} catch (_: Exception) {
		Destination.Login
	}

	companion object {
		class ServerException : Exception()
		class LocalStorageException : Exception()
	}
}
