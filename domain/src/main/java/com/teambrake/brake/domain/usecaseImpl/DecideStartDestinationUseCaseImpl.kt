package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideStartDestinationUseCaseError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.AuthStatusSuccess
import com.teambrake.brake.domain.model.result.success.OfflineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.OnlineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.PreAuthSuccess
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : DecideStartDestinationUseCase {

	override suspend fun invoke(): BrakeResult<AuthStatusSuccess<Destination>, DecideStartDestinationUseCaseError> {
		// 1. 사용자 상태 가져오기
		val userStatusResult = runCatching {
			tokenRepository.getUserStatus().first()
		}

		if (userStatusResult.isFailure) {
			return BrakeResult.Error(UndefinedExceptionError(userStatusResult.exceptionOrNull() ?: Exception("사용자 상태 가져오기 실패")))
		}

		val userStatus = userStatusResult.getOrThrow()

		// 2. 사용자 상태에 따른 분기 처리
		return when (userStatus) {
			// 2-1. 비활성 상태인 경우 모든 데이터 삭제 후 로그인 화면으로 이동
			UserStatus.INACTIVE -> {
				val clearResult = clearAllData()
				when (clearResult) {
					is BrakeResult.Success -> {
						BrakeResult.Success(PreAuthSuccess(Destination.Login))
					}
					is BrakeResult.Error -> {
						clearResult
					}
				}
			}
			// 2-2. 오프라인 상태인 경우
			UserStatus.OFFLINE -> {
				// 3. 닉네임 가져오기
				val nicknameResult = runCatching {
					nicknameRepository.getNickname().first()
				}

				if (nicknameResult.isFailure) {
					return BrakeResult.Error(UndefinedExceptionError(nicknameResult.exceptionOrNull() ?: Exception("닉네임 가져오기 실패")))
				}

				// 4. 온보딩 상태 확인
				val destination = getDestinationByOnboardingStatus()
					?: return BrakeResult.Error(UndefinedExceptionError(Exception("온보딩 상태 확인 실패")))
				BrakeResult.Success(OfflineAuthorizedSuccess(destination))
			}
			// 2-3. 활성 상태 또는 반회원 상태인 경우
			UserStatus.ACTIVE, UserStatus.HALF_SIGNUP -> {
				// 3. 닉네임 가져오기
				val nicknameResult = runCatching {
					nicknameRepository.getNickname().first()
				}

				if (nicknameResult.isFailure) {
					return BrakeResult.Error(UndefinedExceptionError(nicknameResult.exceptionOrNull() ?: Exception("닉네임 가져오기 실패")))
				}

				if (userStatus == UserStatus.ACTIVE) {
					// 4. 온보딩 상태 확인
					val destination = getDestinationByOnboardingStatus()
						?: return BrakeResult.Error(UndefinedExceptionError(Exception("온보딩 상태 확인 실패")))
					BrakeResult.Success(OnlineAuthorizedSuccess(destination))
				} else {
					// 반회원 상태인 경우 로그인 화면으로 이동
					BrakeResult.Success(OnlineAuthorizedSuccess(Destination.Login))
				}
			}
		}
	}

	private suspend fun getDestinationByOnboardingStatus(): Destination? {
		val result = runCatching {
			authRepository.getOnboardingFlag().first()
		}

		if (result.isFailure) {
			return null
		}

		val isOnboardingCompleted = result.getOrNull() ?: return null
		return if (isOnboardingCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	}

	private suspend fun clearAllData(): BrakeResult<Unit, DecideStartDestinationUseCaseError> {
		val appGroupResult = appGroupRepository.clearAppGroup()
		when {
			appGroupResult.isSuccess -> {
				// 성공적으로 앱 그룹 삭제됨
			}
			appGroupResult.isFailure -> {
				val exception = appGroupResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 그룹 삭제 실패")))
			}
		}

		val appResult = appRepository.clearApps()
		when {
			appResult.isSuccess -> {
				// 성공적으로 앱 삭제됨
			}
			appResult.isFailure -> {
				val exception = appResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("앱 삭제 실패")))
			}
		}

		return BrakeResult.Success(Unit)
	}
}
