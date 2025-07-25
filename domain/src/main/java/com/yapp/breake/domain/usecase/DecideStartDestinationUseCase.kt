package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination

interface DecideStartDestinationUseCase {
	operator fun invoke(): Destination
}
