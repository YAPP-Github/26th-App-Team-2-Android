package com.teambrake.brake.core.model.app

import java.time.Duration
import java.time.LocalDate

data class Statistics(
	val date: LocalDate,
	val dayOfWeek: String,
	val actualTime: Duration,
	val goalTime: Duration,
) {

	val usageHours: Long
		get() = actualTime.toHours()

	val usageMinutes: Long
		get() = actualTime.toMinutes()
}
