package com.teambrake.brake.core.ui

import androidx.compose.runtime.Immutable

sealed interface SnackBarState {
	@Immutable
	data class Success(val uiString: UiString) : SnackBarState

	@Immutable
	data class Error(val uiString: UiString) : SnackBarState
}
