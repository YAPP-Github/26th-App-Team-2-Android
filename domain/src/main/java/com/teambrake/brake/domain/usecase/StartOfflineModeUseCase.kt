package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.StartOfflineModeUseCaseError

interface StartOfflineModeUseCase {
	suspend operator fun invoke(offlineNickname: String): BrakeResult<Destination, StartOfflineModeUseCaseError>
}
