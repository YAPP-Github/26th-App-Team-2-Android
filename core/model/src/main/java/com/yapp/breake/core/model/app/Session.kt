package com.yapp.breake.core.model.app

import java.time.LocalDateTime

data class Session(
	val groupId: Long,
	val startTime: LocalDateTime,
	val endTime: LocalDateTime,
	val goalMinutes: Int,
	val snoozeUnit: Int,
	val snoozeCount: Int,
)
