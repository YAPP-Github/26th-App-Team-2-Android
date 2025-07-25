package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination

interface DecideNextDestinationFromPermissionUseCase {
	operator fun invoke(onError: suspend (Throwable) -> Unit): Destination
}
