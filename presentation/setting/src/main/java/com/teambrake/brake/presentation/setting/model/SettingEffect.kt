package com.teambrake.brake.presentation.setting.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface SettingEffect {
	@Immutable
	data object NavigateToLogin : SettingEffect

	@Immutable
	data object NavigateToNickname : SettingEffect

	@Immutable
	data object NavigateToOpinion : SettingEffect

	@Immutable
	data object NavigateToInquiry : SettingEffect

	@Immutable
	data object NavigateToTermsOfService : SettingEffect

	@Immutable
	data object NavigateToPrivacyPolicy : SettingEffect
}
