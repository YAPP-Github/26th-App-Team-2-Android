package com.yapp.breake.domain.usecase

import java.time.LocalDateTime

interface SetSnoozeAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
		appName: String,
	): Result<LocalDateTime>
}

