package com.yapp.breake.core.common

object Constants {

	const val MAX_SNOOZE_COUNT = 2
	const val BLOCKING_TIME = 5 * 60
	const val TEST_BLOCKING_TIME = 10
	const val SNOOZE_TIME = 3 * 60
	val SNOOZE_MINUTES get() = SNOOZE_TIME / 60
	const val TEST_SNOOZE_TIME = 10
}
