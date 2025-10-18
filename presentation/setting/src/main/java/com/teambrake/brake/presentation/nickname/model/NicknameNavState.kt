package com.teambrake.brake.presentation.nickname.model

import androidx.compose.runtime.Immutable

sealed interface NicknameNavState {

	@Immutable
	data object PopBackStack : NicknameNavState

	@Immutable
	data object NavigateToSetting : NicknameNavState
}
