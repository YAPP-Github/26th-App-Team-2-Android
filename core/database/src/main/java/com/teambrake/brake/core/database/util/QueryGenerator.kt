package com.teambrake.brake.core.database.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KProperty1

fun <T : Any> generateInsertQuery(entity: T, tableName: String): String {
	val kClass = entity::class
	val columns = mutableListOf<String>()
	val values = mutableListOf<String>()

	kClass.members.filterIsInstance<KProperty1<T, *>>().forEach { property ->
		columns.add(property.name)
		values.add(formatValue(property.get(entity)))
	}

	val columnsString = columns.joinToString(", ")
	val valuesString = values.joinToString(", ")

	return "INSERT INTO $tableName ($columnsString) VALUES ($valuesString);"
}

private fun formatValue(value: Any?): String {
	return when (value) {
		is String -> "'$value'"
		is Boolean -> if (value) "1" else "0"
		is LocalDate -> "'${value.format(DateTimeFormatter.ISO_LOCAL_DATE)}'"
		else -> value.toString()
	}
}
