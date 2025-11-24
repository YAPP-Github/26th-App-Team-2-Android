package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideStartDestinationUseCaseError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.ModeSuccess
import com.teambrake.brake.domain.model.result.success.OfflineModeSuccess
import com.teambrake.brake.domain.model.result.success.OnlineModeSuccess
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named

class DecideStartDestinationUseCaseImpl @Inject constructor(
	private val sessionRepository: SessionRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DecideStartDestinationUseCase {

	override suspend fun invoke(): BrakeResult<ModeSuccess<Destination>, DecideStartDestinationUseCaseError> {
		try {
			// 1. 원격 사용자 정보 가져오기
			nicknameRepository.getRemoteUserName().let { result ->
				when (result) {
					is BrakeResult.Success -> result.data.let { success ->
						when (success) {
							// 1-1. 오프라인 모드인 경우 로그인 화면으로 이동
							is OfflineModeSuccess -> {
								val destination = getDestinationByOnboardingStatus()
								return BrakeResult.Success(OfflineModeSuccess(destination))
							}
							// 1-2. 온라인 모드인 경우
							is OnlineModeSuccess -> {
								// 2. 사용자 상태에 따른 분기 처리
								when (success.data.state) {
									// 2-1. 활성 상태인 경우
									UserStatus.ACTIVE -> {
										// 3. 온보딩 상태 확인
										val destination = getDestinationByOnboardingStatus()
										return BrakeResult.Success(OnlineModeSuccess(destination))
									}
									// 2-2. 비회원 상태인 경우
									else -> {
										// 비활성 상태인 경우 모든 데이터 삭제 후 로그인 화면으로 이동
										clearAllData()
										return BrakeResult.Success(OnlineModeSuccess(Destination.Login))
									}
								}
							}
						}
					}

					is BrakeResult.Error -> {
						return result
					}
				}
			}
		} catch (e: Exception) {
			return BrakeResult.Error(UndefinedExceptionError(e))
		}
	}

	private suspend fun getDestinationByOnboardingStatus(): Destination {
		val isCompleted = sessionRepository.getOnboardingFlag { e ->
			throw LocalStorageException(e.message ?: "Local storage error")
		}.firstOrNull() == true

		return if (isCompleted) {
			Destination.PermissionOrHome
		} else {
			Destination.Onboarding
		}
	}

	private suspend fun clearAllData() {
		appGroupRepository.clearAppGroup()
		appRepository.clearApps()
	}

	// TODO: 간단하게 흐름을 처리하기 위해 예외 처리로 구현. 그러나 분기 처리(Result)와 예외 처리(Exception)의 구분이 필요하므로 추후 리팩토링 필요
	companion object {
		class LocalStorageException(override val message: String) : Exception()
	}
}
