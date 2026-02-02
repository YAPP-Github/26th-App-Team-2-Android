package com.teambrake.brake.domain.usecase

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DeleteGroupUseCaseError

interface DeleteGroupUseCase {
	suspend operator fun invoke(
		groupId: Long,
	): BrakeResult<Unit, DeleteGroupUseCaseError>
}
