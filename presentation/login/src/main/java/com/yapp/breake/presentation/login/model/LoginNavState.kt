package com.yapp.breake.presentation.login.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginNavState {
	@Immutable
	data object NavigateToPrivacyPolicy : LoginNavState

	@Immutable
	data object NavigateToTermsOfService : LoginNavState

	@Immutable
	data object NavigateToHome : LoginNavState

	@Immutable
	data object NavigateToSignup : LoginNavState

	@Immutable
	data object NavigateToPermission : LoginNavState

	@Immutable
	data object NavigateToOnboarding : LoginNavState
}
