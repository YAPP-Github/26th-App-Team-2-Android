package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DeleteAccountUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.usecase.DeleteAccountUseCase
import javax.inject.Inject

class DeleteAccountUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
	private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DeleteAccountUseCase {
	override suspend fun invoke(): BrakeResult<Destination, DeleteAccountUseCaseError> =
		when (val modeResult = authRepository.clearRemoteAccount()) {
			// 1-1. Remote 계정 삭제 성공 시 로컬 데이터 스토어 전체 삭제
			is BrakeResult.Success -> {
				when (val localResult = authRepository.clearAuthDataStore()) {
					// 2-1. Local 데이터 스토어 전체 삭제 성공 시 로그인 화면으로 이동
					is BrakeResult.Success -> {
						nicknameRepository.clearLocalName {}
						appGroupRepository.clearAppGroup()
						appRepository.clearApps()
						BrakeResult.Success(Destination.Login)
					}

					// 2-2. Local 데이터 스토어 전체 삭제 실패 시 아무 동작도 하지 않음
					// (이론상 발생하지 않아야 함)
					is BrakeResult.Error -> {
						BrakeResult.Error(localResult.error)
					}
				}
			}

			// 1-2. Remote 계정 삭제 실패 시 아무 동작도 하지 않음
			is BrakeResult.Error -> {
				BrakeResult.Error(modeResult.error)
			}
		}
}
