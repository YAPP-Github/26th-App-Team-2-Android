package com.teambrake.brake.domain.usecase

import com.teambrake.brake.core.model.user.Destination

interface StartOfflineModeUseCase {
	suspend operator fun invoke(
		offlineNickname: String,
		onError: suspend (Throwable) -> Unit,
	): Destination
}
