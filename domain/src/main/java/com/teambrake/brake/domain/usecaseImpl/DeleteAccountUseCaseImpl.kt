package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DeleteAccountUseCaseError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.usecase.DeleteAccountUseCase
import javax.inject.Inject
import javax.inject.Named

class DeleteAccountUseCaseImpl @Inject constructor(
	private val authRepository: AuthRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : DeleteAccountUseCase {
	override suspend fun invoke(): BrakeResult<Destination, DeleteAccountUseCaseError> {
		// 1. Remote 계정 삭제
		val remoteResult = authRepository.clearRemoteAccount()
		when {
			remoteResult.isSuccess -> {
				// 성공적으로 원격 계정 삭제됨
			}
			remoteResult.isFailure -> {
				val exception = remoteResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("원격 계정 삭제 실패")))
			}
		}

		// 2. Local 데이터 스토어 전체 삭제
		val authResult = authRepository.clearAuthDataStore()
		when {
			authResult.isSuccess -> {
				// 성공적으로 인증 데이터 삭제됨
			}
			authResult.isFailure -> {
				val exception = authResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("인증 데이터 삭제 실패")))
			}
		}

		val nicknameResult = nicknameRepository.clearLocalName()
		when {
			nicknameResult.isSuccess -> {
				// 성공적으로 닉네임 삭제됨
			}
			nicknameResult.isFailure -> {
				val exception = nicknameResult.exceptionOrNull()
				return BrakeResult.Error(LocalApiCallError(exception ?: Exception("닉네임 삭제 실패")))
			}
		}

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

		return BrakeResult.Success(Destination.Login)
	}
}
