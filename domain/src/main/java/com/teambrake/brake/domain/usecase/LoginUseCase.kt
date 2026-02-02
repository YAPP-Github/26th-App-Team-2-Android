package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LoginUseCaseError
import kotlinx.coroutines.flow.Flow

/**
 * 카카오 로그인 이후 AuthCode를 이용하여 로그인하는 UseCase
 *
 * 로그인 성공 시 UserStatus를 반환하며, 실패 시 Error 반환
 */
interface LoginUseCase {
	operator fun invoke(
		authCode: String,
		provider: String,
	): Flow<BrakeResult<UserStatus, LoginUseCaseError>>
}
