package com.yapp.breake.core.model.app

import java.time.LocalDate

data class Statistics(
	val date: LocalDate,
	val dayOfWeek: String,
	val actualTime: String,
	val goalTime: String,
)
