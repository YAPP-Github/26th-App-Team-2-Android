package com.teambrake.brake.presentation.legal.privacy.model

import androidx.compose.runtime.Immutable

sealed interface PrivacyUiState {
	@Immutable
	data object PrivacyIdle : PrivacyUiState

	@Immutable
	data object PrivacyError : PrivacyUiState
}
