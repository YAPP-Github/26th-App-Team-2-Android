package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroup

interface ResetAppGroupUsecase {
	suspend operator fun invoke(
		appGroup: AppGroup,
	): Result<Unit>
}
