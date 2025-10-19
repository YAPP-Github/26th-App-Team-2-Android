package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup

interface ResetAppGroupUsecase {
	suspend operator fun invoke(
		appGroup: AppGroup,
	): Result<Unit>
}
