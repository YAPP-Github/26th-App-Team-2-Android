package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.Destination

interface DecideStartDestinationUseCase {
	suspend operator fun invoke(): Destination
}
