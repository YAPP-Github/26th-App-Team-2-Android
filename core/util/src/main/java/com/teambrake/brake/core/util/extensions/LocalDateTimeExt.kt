package com.teambrake.brake.core.util.extensions

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.time.temporal.ChronoUnit

fun LocalDateTime.toLocalizedTime(locale: Locale = Locale.getDefault()): String = when (locale.language) {
	Locale.KOREAN.language -> {
		val formatter = DateTimeFormatter.ofPattern("h시 mm분", locale)
		this.format(formatter)
	}
	else -> {
		val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
			.withLocale(locale)
		this.format(formatter)
	}
}

fun LocalDateTime.toSimpleTime(): String {
	val formatter = DateTimeFormatter.ofPattern("HH:mm")
	return this.format(formatter)
}

fun getRemainingSeconds(
	endTime: LocalDateTime?,
): Long {
	if (endTime == null) return 0L
	val remaining = ChronoUnit.SECONDS.between(LocalDateTime.now(), endTime)
	return maxOf(0L, remaining)
}

fun Long.toMinutesAndSeconds(): Pair<Long, Long> {
	val minutes = this / 60
	val seconds = this % 60
	return Pair(minutes, seconds)
}

fun LocalDate.toShortDateFormat(): String {
	val formatter = DateTimeFormatter.ofPattern("M/d")
	return this.format(formatter)
}
