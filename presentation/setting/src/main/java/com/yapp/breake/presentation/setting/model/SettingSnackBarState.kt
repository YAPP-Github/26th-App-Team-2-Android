package com.yapp.breake.presentation.setting.model

import androidx.compose.runtime.Immutable
import com.yapp.breake.core.ui.UiString

sealed interface SettingSnackBarState {
	@Immutable
	data class Success(val uiString: UiString) : SettingSnackBarState

	@Immutable
	data class Error(val uiString: UiString) : SettingSnackBarState
}
