package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination

interface LogoutUseCase {
	suspend operator fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Destination
}
