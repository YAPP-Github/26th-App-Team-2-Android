package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.UpdateNicknameUseCaseError

/**
 * 회원 가입 시 또는 닉네임 변경 시 사용되는 UseCase
 */
interface UpdateNicknameUseCase {
	suspend operator fun invoke(nickname: String): BrakeResult<Unit, UpdateNicknameUseCaseError>
}
