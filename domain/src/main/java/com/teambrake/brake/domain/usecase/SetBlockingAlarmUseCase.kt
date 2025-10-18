package com.teambrake.brake.domain.usecase

import java.time.LocalDateTime

interface SetBlockingAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
	): Result<LocalDateTime>
}
