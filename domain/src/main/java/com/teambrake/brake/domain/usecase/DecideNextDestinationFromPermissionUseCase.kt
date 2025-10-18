package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination

interface DecideNextDestinationFromPermissionUseCase {
	operator fun invoke(onError: suspend (Throwable) -> Unit): Destination
}
