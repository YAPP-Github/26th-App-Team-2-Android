package com.teambrake.brake.core.util

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

object DateUtil {

	fun getShortDayOfWeekNames(locale: Locale = Locale.getDefault()): List<String> = listOf(
		DayOfWeek.MONDAY,
		DayOfWeek.TUESDAY,
		DayOfWeek.WEDNESDAY,
		DayOfWeek.THURSDAY,
		DayOfWeek.FRIDAY,
		DayOfWeek.SATURDAY,
		DayOfWeek.SUNDAY,
	).map { dayOfWeek ->
		dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replace(".", "")
	}

	fun getDayOfWeekName(dayOfWeek: DayOfWeek, locale: Locale = Locale.getDefault()): String = dayOfWeek.getDisplayName(TextStyle.SHORT, locale).replace(".", "")
}
