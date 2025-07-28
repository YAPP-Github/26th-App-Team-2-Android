package com.yapp.breake.presentation.setting.model

data class SettingUser(
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
