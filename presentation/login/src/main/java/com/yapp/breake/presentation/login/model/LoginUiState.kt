package com.yapp.breake.presentation.login.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginUiState {

	@Immutable
	data object LoginIdle : LoginUiState

	@Immutable
	data object LoginAsRegistered : LoginUiState

	@Immutable
	data object LoginAsNewUser : LoginUiState
}
