package com.yapp.breake.presentation.setting.model

data class SettingAppInfo(
	val version: String,
	val privacyPolicy: String,
	val termsOfService: String,
) {
	companion object {
		val EMPTY = SettingAppInfo(
			version = "0.0.0",
			privacyPolicy = "",
			termsOfService = "",
		)
	}
}
