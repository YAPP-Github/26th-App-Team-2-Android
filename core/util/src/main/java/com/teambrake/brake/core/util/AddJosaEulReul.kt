package com.teambrake.brake.core.util

fun String.addJosaEulReul(): String {
	val appLocales = androidx.core.os.LocaleListCompat.getAdjustedDefault()
	val isKorean = if (!appLocales.isEmpty) {
		appLocales[0]?.language == "ko"
	} else {
		androidx.core.os.ConfigurationCompat.getLocales(android.content.res.Resources.getSystem().configuration)[0]?.language == "ko"
	}

	if (!isKorean) return this

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
