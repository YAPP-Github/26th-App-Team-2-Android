package com.teambrake.brake.presentation.report.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teambrake.brake.core.model.app.Statistics
import java.time.Duration
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@Stable
internal sealed interface ReportUiState {

	@Immutable
	data object Loading : ReportUiState

	@Immutable
	data object Error : ReportUiState

	@Immutable
	data class Success(
		val statisticList: List<Statistics>,
	) : ReportUiState {

		private val todayUsageTime: Duration
			get() {
				val today = LocalDate.now()
				return statisticList.find { it.date == today }?.actualTime ?: Duration.ZERO
			}

		val todayUsageHours: Long
			get() = todayUsageTime.toHours()

		val todayUsageMinutes: Long
			get() = todayUsageTime.toMinutes() % 60

		val weeklyStatistics: List<Statistics>
			get() {
				val today = LocalDate.now()
				val startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

				return (0..6).map { dayOffset ->
					val currentDate = startOfWeek.plusDays(dayOffset.toLong())
					val dayOfWeek = currentDate.dayOfWeek

					statisticList.find { it.date == currentDate } ?: Statistics(
						date = currentDate,
						dayOfWeek = dayOfWeek.name,
						actualTime = Duration.ZERO,
						goalTime = Duration.ZERO,
					)
				}
			}
	}
}
