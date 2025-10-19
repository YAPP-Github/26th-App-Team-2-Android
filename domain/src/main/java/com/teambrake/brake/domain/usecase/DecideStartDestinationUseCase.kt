package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination

interface DecideStartDestinationUseCase {
	suspend operator fun invoke(): Destination
}
