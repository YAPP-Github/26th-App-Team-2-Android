package com.teambrake.brake.presentation.legal.terms.model

import androidx.compose.runtime.Immutable

sealed interface TermsUiState {
	@Immutable
	data object PrivacyIdle : TermsUiState

	@Immutable
	data object PrivacyError : TermsUiState
}
