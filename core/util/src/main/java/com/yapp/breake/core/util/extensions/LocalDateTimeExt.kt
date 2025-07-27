package com.yapp.breake.core.util.extensions

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun LocalDateTime.toLocalizedTime(locale: Locale = Locale.getDefault()): String {
	return when (locale.language) {
		Locale.KOREAN.language -> {
			val formatter = DateTimeFormatter.ofPattern("a h시 mm분", locale)
			this.format(formatter)
		}
		else -> {
			val formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
				.withLocale(locale)
			this.format(formatter)
		}
	}
}

fun LocalDateTime.toSimpleTime(): String {
	val formatter = DateTimeFormatter.ofPattern("HH:mm")
	return this.format(formatter)
}
