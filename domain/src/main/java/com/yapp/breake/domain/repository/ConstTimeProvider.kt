package com.yapp.breake.domain.repository

interface ConstTimeProvider {
	val snoozeTime: Long
	val blockingTime: Long
	fun getTime(seconds: Long): Long
}
