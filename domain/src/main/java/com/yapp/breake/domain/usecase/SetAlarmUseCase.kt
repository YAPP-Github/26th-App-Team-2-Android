package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.app.AppGroupState
import java.time.LocalDateTime

interface SetAlarmUseCase {
	suspend operator fun invoke(
		groupId: Long,
		appName: String,
		appGroupState: AppGroupState,
		second: Int = 0,
		isUsingApp: Boolean = false,
	): Result<LocalDateTime>
}
