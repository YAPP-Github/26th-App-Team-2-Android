package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup

interface CreateNewGroupUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
		group: AppGroup,
	)
}
