package com.teambrake.brake.domain.usecase

import java.time.LocalDateTime

interface SetSnoozeAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
		groupName: String,
	): Result<LocalDateTime>
}
