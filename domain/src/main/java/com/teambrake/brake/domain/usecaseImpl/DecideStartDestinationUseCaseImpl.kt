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
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DecideStartDestinationUseCase {

	override suspend fun invoke(): BrakeResult<AuthStatusSuccess<Destination>, DecideStartDestinationUseCaseError> {
		try {
			// 1. 원격 사용자 정보 가져오기
			nicknameRepository.getRemoteUserName().let { result ->
				when (result) {
					is BrakeResult.Success -> result.data.let { success ->
						when (success) {
							// 1-1. 오프라인 모드인 경우 로그인 화면으로 이동
							is OfflineAuthorizedSuccess -> {
								val destination = getDestinationByOnboardingStatus()
								return BrakeResult.Success(OfflineAuthorizedSuccess(destination))
							}
							// 1-2. 온라인 모드인 경우
							is OnlineAuthorizedSuccess -> {
								// 2. 사용자 상태에 따른 분기 처리
								when (success.data.state) {
									// 2-1. 활성 상태인 경우
									UserStatus.ACTIVE -> {
										// 3. 온보딩 상태 확인
										val destination = getDestinationByOnboardingStatus()
										return BrakeResult.Success(OnlineAuthorizedSuccess(destination))
									}
									// 2-2. 비회원 상태인 경우
									else -> {
										// 비활성 상태인 경우 모든 데이터 삭제 후 로그인 화면으로 이동
										clearAllData()
										return BrakeResult.Success(OnlineAuthorizedSuccess(Destination.Login))
									}
								}
							}

							is PreAuthSuccess -> {
								// 1-3. 사전 인증 상태인 경우 로그인 화면으로 이동
								return BrakeResult.Success(PreAuthSuccess(Destination.Login))
							}
						}
					}

					is BrakeResult.Error -> {
						return result
					}
				}
			}
		} catch (e: LocalStorageException) {
			return BrakeResult.Error(LocalApiCallError(e.e))
		} catch (e: Exception) {
			return BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	private suspend fun getDestinationByOnboardingStatus(): Destination =
		when (val result = authRepository.getOnboardingFlag()) {
			is BrakeResult.Success -> {
				val isOnboardingCompleted = result.data
				if (isOnboardingCompleted) {
					Destination.PermissionOrHome
				} else {
					Destination.Onboarding
				}
			}

			is BrakeResult.Error -> {
				throw LocalStorageException(result.error.e)
			}
		}

	private suspend fun clearAllData() {
		appGroupRepository.clearAppGroup()
		appRepository.clearApps()
	}

	companion object {
		class LocalStorageException(val e: Throwable) : Exception()
	}
}
