package com.teambrake.brake.core.common

object Constants {
	const val MAX_SNOOZE_COUNT = 2

	const val BLOCKING_TIME: Long = 180
	const val TEST_BLOCKING_TIME: Long = 10

	const val SNOOZE_TIME: Long = 300
	const val TEST_SNOOZE_TIME: Long = 10
	val SNOOZE_MINUTES get() = SNOOZE_TIME / 60
}
