package com.yapp.breake.core.util

fun String.addJosaEulReul(): String {
	if (this.isEmpty()) return "${this}를"

	val lastChar = this.last()

	return when {
		lastChar.code in 0xAC00..0xD7A3 -> {
			if ((lastChar.code - 0xAC00) % 28 != 0) "${this}을" else "${this}를"
		}

		lastChar.isLetter() -> {
			when (lastChar.lowercaseChar()) {
				'a', 'e', 'i', 'o', 'u', 'l', 'm', 'n' -> "${this}을"
				else -> "${this}를"
			}
		}

		lastChar.isDigit() -> {
			when (lastChar) {
				'0' -> "${this}을"
				'1', '3', '6', '7', '8' -> "${this}을"
				'2', '4', '5', '9' -> "${this}를"
				else -> "${this}를"
			}
		}

		else -> "${this}를"
	}
}
