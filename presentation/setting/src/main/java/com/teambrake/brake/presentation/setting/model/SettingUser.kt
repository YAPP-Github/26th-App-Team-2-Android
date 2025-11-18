package com.teambrake.brake.presentation.setting.model

internal data class SettingUser(
	val imageUrl: String?,
	val name: String,
) {
	companion object {
		val EMPTY = SettingUser(
			imageUrl = null,
			name = "",
		)
	}
}
