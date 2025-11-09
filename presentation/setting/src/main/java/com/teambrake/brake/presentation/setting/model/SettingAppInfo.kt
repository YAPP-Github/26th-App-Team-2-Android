package com.teambrake.brake.presentation.setting.model

import com.teambrake.brake.presentation.setting.BuildConfig

data class SettingAppInfo(
	val version: String,
	val privacyPolicy: String,
	val termsOfService: String,
) {
	companion object {
		val EMPTY = SettingAppInfo(
			version = BuildConfig.VERSION_NAME,
			privacyPolicy = "",
			termsOfService = "",
		)
	}
}
