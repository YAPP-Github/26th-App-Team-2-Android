package com.teambrake.brake.presentation.setting.model

import androidx.compose.runtime.Stable

@Stable
internal data class SettingUiState(
	val user: SettingUser,
	val appInfo: SettingAppInfo,
	val status: Status,
) {
	enum class Status {
		Idle,
		Loaded,
		LogoutWarning,
		DeleteWarning,
		DeletingAccount,
	}

	companion object {
		val Idle = SettingUiState(
			user = SettingUser.EMPTY,
			appInfo = SettingAppInfo.EMPTY,
			status = Status.Idle,
		)
	}
}
