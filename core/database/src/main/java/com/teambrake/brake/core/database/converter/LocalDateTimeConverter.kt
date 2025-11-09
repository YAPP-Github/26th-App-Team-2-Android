package com.teambrake.brake.core.database.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class LocalDateTimeConverter {

	private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

	@TypeConverter
	fun fromLocalDateTime(dateTime: LocalDateTime?): String? = dateTime?.format(formatter)

	@TypeConverter
	fun toLocalDateTime(dateTimeString: String?): LocalDateTime? = dateTimeString?.let {
		LocalDateTime.parse(it, formatter)
	}
}
