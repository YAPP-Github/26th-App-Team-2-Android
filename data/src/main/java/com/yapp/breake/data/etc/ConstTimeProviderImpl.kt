package com.yapp.breake.data.etc

import com.yapp.breake.core.common.Constants
import com.yapp.breake.data.BuildConfig
import com.yapp.breake.domain.etc.ConstTimeProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstTimeProviderImpl @Inject constructor() : ConstTimeProvider {

	private val isDebug get() = BuildConfig.DEBUG

	override val snoozeTime: Long = if (isDebug) {
		Constants.TEST_SNOOZE_TIME
	} else {
		Constants.SNOOZE_TIME
	}

	override val blockingTime: Long = if (isDebug) {
		Constants.TEST_BLOCKING_TIME
	} else {
		Constants.BLOCKING_TIME
	}

	override fun getTime(seconds: Long): Long {
		return if (isDebug) {
			seconds
		} else {
			seconds * 60
		}
	}
}
