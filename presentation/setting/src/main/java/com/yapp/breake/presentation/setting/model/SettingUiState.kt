package com.yapp.breake.presentation.setting.model

import androidx.compose.runtime.Stable

@Stable
sealed interface SettingUiState {
	val user: SettingUser
	val appInfo: SettingAppInfo

	data class SettingIdle(
		override val user: SettingUser = SettingUser.EMPTY,
		override val appInfo: SettingAppInfo = SettingAppInfo.EMPTY,
	) : SettingUiState

	data class SettingLoaded(
		override val user: SettingUser,
		override val appInfo: SettingAppInfo,
	) : SettingUiState

	data class SettingLogoutWarning(
		override val user: SettingUser,
		override val appInfo: SettingAppInfo,
	) : SettingUiState

	data class SettingDeleteWarning(
		override val user: SettingUser,
		override val appInfo: SettingAppInfo,
	) : SettingUiState

	data class SettingDeletingAccount(
		override val user: SettingUser,
		override val appInfo: SettingAppInfo,
	) : SettingUiState
}
