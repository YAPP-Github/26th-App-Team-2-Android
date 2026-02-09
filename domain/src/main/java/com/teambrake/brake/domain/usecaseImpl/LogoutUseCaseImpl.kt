package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.LogoutUseCaseError
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.LogoutUseCase
import javax.inject.Inject
import javax.inject.Named

class LogoutUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val authRepository: AuthRepository,
	private val appGroupRepository: AppGroupRepository,
	private val appRepository: AppRepository,
) : LogoutUseCase {
	override suspend fun invoke(): BrakeResult<Destination, LogoutUseCaseError> {
		val logoutResult = tokenRepository.logoutRemoteAccount()

		when {
			logoutResult.isSuccess -> {
				// 성공적으로 로그아웃됨
			}

			logoutResult.isFailure -> {
				// 로그아웃 실패해도 로컬 데이터는 삭제 시도
			}
		}

		// Local 데이터 스토어 전체 삭제
		val authResult = authRepository.clearAuthDataStore()
		when {
			authResult.isSuccess -> {
				// 성공적으로 인증 데이터 삭제됨
			}

			authResult.isFailure -> {
				val exception = authResult.exceptionOrNull()
				return BrakeResult.Error(
					LocalApiCallError(
						exception ?: Exception("인증 데이터 삭제 실패"),
					),
				)
			}
		}

		val nicknameResult = nicknameRepository.clearLocalName()
		when {
			nicknameResult.isSuccess -> {
				// 성공적으로 닉네임 삭제됨
			}

			nicknameResult.isFailure -> {
				val exception = nicknameResult.exceptionOrNull()
				return BrakeResult.Error(
					LocalApiCallError(
						exception ?: Exception("닉네임 삭제 실패"),
					),
				)
			}
		}

		val appGroupResult = appGroupRepository.clearAppGroup()
		when {
			appGroupResult.isSuccess -> {
				// 성공적으로 앱 그룹 삭제됨
			}

			appGroupResult.isFailure -> {
				val exception = appGroupResult.exceptionOrNull()
				return BrakeResult.Error(
					LocalApiCallError(
						exception ?: Exception("앱 그룹 삭제 실패"),
					),
				)
			}
		}

		val appResult = appRepository.clearApps()
		when {
			appResult.isSuccess -> {
				// 성공적으로 앱 삭제됨
			}

			appResult.isFailure -> {
				val exception = appResult.exceptionOrNull()
				return BrakeResult.Error(
					LocalApiCallError(
						exception ?: Exception("앱 삭제 실패"),
					),
				)
			}
		}

		return BrakeResult.Success(Destination.Login)
	}
}
