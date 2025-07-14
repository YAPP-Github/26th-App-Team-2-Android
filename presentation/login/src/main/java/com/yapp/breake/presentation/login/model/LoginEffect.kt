package com.yapp.breake.presentation.login.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginEffect {

	@Immutable
	data object NavigateToHome : LoginEffect

	@Immutable
	data object NavigateToSignup : LoginEffect
}
