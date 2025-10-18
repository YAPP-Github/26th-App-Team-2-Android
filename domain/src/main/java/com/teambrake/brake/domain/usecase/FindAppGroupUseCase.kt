package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.app.AppGroup

interface FindAppGroupUseCase {
	suspend operator fun invoke(packageName: String): AppGroup?
}
