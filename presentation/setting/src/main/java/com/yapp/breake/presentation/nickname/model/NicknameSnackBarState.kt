package com.yapp.breake.presentation.nickname.model

import androidx.compose.runtime.Immutable
import com.yapp.breake.core.util.UiString

sealed interface NicknameSnackBarState {
	@Immutable
	data class Success(val uiString: UiString) : NicknameSnackBarState

	@Immutable
	data class Error(val uiString: UiString) : NicknameSnackBarState
}
