package com.yapp.breake.domain.etc

interface ConstTimeProvider {
	val snoozeTime: Long
	val blockingTime: Long
	fun getTime(seconds: Long): Long
}
