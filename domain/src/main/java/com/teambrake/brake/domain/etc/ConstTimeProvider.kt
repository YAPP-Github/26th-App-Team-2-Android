package com.teambrake.brake.domain.etc

interface ConstTimeProvider {
	val snoozeTime: Long
	val blockingTime: Long
	fun getTime(seconds: Long): Long
}
