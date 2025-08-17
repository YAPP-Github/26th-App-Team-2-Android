package com.yapp.breake.data.mapper

import com.yapp.breake.core.model.app.Statistics
import com.yapp.breake.data.remote.model.StatisticData
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

internal fun StatisticData.toStatistics(): List<Statistics> {
	return statistics.map { statistic ->
		Statistics(
			date = LocalDate.parse(statistic.date, dateFormatter),
			dayOfWeek = statistic.dayOfWeek,
			actualTime = statistic.actualTime,
			goalTime = statistic.goalTime,
		)
	}
}

internal fun LocalDate.toDateString(): String {
	return this.format(dateFormatter)
}
