package com.teambrake.brake.data.mapper

import com.teambrake.brake.core.model.app.Statistics
import com.teambrake.brake.data.remote.model.StatisticData
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

internal fun StatisticData.toStatistics(): List<Statistics> = statistics.map { statistic ->
	Statistics(
		date = LocalDate.parse(statistic.date, dateFormatter),
		dayOfWeek = statistic.dayOfWeek,
		actualTime = stringToDuration(statistic.actualTime),
		goalTime = stringToDuration(statistic.goalTime),
	)
}

private fun stringToDuration(timeString: String): Duration {
	val localTime = LocalTime.parse(timeString, timeFormatter)
	return Duration.between(LocalTime.MIDNIGHT, localTime)
}

internal fun LocalDate.toDateString(): String = this.format(dateFormatter)
