package com.teambrake.brake.presentation.signup.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface SignupUiState {

	val name: String

	@Immutable
	data class SignupIdle(override val name: String) : SignupUiState

	@Immutable
	data class SignupNameRegistering(override val name: String) : SignupUiState
}
