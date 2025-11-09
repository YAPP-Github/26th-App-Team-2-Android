package com.teambrake.brake.presentation.signup.model

import androidx.compose.runtime.Immutable

interface SignupEffect {

	@Immutable
	data object NavigateToBack : SignupEffect

	@Immutable
	data object NavigateToOnboarding : SignupEffect
}
