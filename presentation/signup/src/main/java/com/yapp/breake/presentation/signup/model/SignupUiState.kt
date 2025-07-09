package com.yapp.breake.presentation.signup.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface SignupUiState {

	val name: String
		get() = ""

	@Immutable
	data class SignupIdle(override val name: String) : SignupUiState

	@Immutable
	data class SignupTypedName(override val name: String) : SignupUiState

	@Immutable
	data object SignupSuccess : SignupUiState
}
