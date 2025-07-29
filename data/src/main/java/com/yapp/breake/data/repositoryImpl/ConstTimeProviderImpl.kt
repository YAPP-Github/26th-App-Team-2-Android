package com.yapp.breake.data.repositoryImpl

import com.yapp.breake.core.common.Constants
import com.yapp.breake.data.BuildConfig
import com.yapp.breake.domain.repository.ConstTimeProvider
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConstTimeProviderImpl @Inject constructor() : ConstTimeProvider {
	override val snoozeTime: Long = if (BuildConfig.DEBUG) {
		Constants.TEST_SNOOZE_TIME
	} else {
		Constants.SNOOZE_TIME
	}

	override val blockingTime: Long = if (BuildConfig.DEBUG) {
		Constants.TEST_BLOCKING_TIME
	} else {
		Constants.BLOCKING_TIME
	}

	override fun getTime(seconds: Long): Long {
		return if (BuildConfig.DEBUG) {
			seconds
		} else {
			seconds * 60
		}
	}
}
