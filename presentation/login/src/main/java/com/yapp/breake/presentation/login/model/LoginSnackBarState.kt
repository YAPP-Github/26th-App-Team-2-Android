package com.yapp.breake.presentation.login.model

import androidx.compose.runtime.Immutable
import com.yapp.breake.core.ui.UiString

interface LoginSnackBarState {
	@Immutable
	data class Success(val uiString: UiString) : LoginSnackBarState

	@Immutable
	data class Error(val uiString: UiString) : LoginSnackBarState
}
