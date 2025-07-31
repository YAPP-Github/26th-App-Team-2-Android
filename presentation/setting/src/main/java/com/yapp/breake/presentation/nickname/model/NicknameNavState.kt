package com.yapp.breake.presentation.nickname.model

import androidx.compose.runtime.Immutable

sealed interface NicknameNavState {

	@Immutable
	data object NavigateToSetting : NicknameNavState
}
