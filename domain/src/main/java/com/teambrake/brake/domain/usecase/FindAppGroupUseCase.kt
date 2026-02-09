package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.FindAppGroupUseCaseError

interface FindAppGroupUseCase {
	suspend operator fun invoke(packageName: String): BrakeResult<AppGroup?, FindAppGroupUseCaseError>
}
