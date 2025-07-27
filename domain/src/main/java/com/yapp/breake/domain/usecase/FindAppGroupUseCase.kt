package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroup

interface FindAppGroupUseCase {
	suspend operator fun invoke(packageName: String): AppGroup?
}
