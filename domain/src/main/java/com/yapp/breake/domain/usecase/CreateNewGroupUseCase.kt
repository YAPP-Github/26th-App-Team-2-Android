package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroup

interface CreateNewGroupUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
		group: AppGroup,
	)
}
