package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.StartOfflineModeUseCaseError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.StartOfflineModeUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class StartOfflineModeUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val authRepository: AuthRepository,
	@Named("TokenRepo") private val localRepository: TokenRepository,
) : StartOfflineModeUseCase {
	override suspend fun invoke(offlineNickname: String): BrakeResult<Destination, StartOfflineModeUseCaseError> {
		val statusResult = localRepository.setOfflineModeStatus()

		when {
			statusResult.isSuccess -> {
				// 성공적으로 오프라인 모드 설정됨
			}

			statusResult.isFailure -> {
				val exception = statusResult.exceptionOrNull()
				return BrakeResult.Error(
					UndefinedExceptionError(
						exception ?: Exception("오프라인 모드 설정 실패"),
					),
				)
			}
		}

		val nicknameResult = nicknameRepository.saveLocalUserName(
			nickname = offlineNickname,
		)

		when {
			nicknameResult.isSuccess -> {
				// 성공적으로 닉네임 저장됨
			}

			nicknameResult.isFailure -> {
				val exception = nicknameResult.exceptionOrNull()
				return BrakeResult.Error(
					UndefinedExceptionError(
						exception ?: Exception("닉네임 저장 실패"),
					),
				)
			}
		}

		val onboardingResult = runCatching {
			authRepository.getOnboardingFlag().first()
		}

		if (onboardingResult.isFailure) {
			return BrakeResult.Error(UndefinedExceptionError(onboardingResult.exceptionOrNull() ?: Exception("온보딩 상태 확인 실패")))
		}

		val isOnboardingCompleted = onboardingResult.getOrThrow()
		val destination = if (isOnboardingCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
		return BrakeResult.Success(destination)
	}
}
