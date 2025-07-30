package com.yapp.breake.presentation.setting.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface SettingEffect {
	@Immutable
	data object NavigateToLogin : SettingEffect

	@Immutable
	data object NavigateToNickname : SettingEffect
}
