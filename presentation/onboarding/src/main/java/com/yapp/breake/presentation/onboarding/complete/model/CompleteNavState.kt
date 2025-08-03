package com.yapp.breake.presentation.onboarding.complete.model

import androidx.compose.runtime.Immutable

interface CompleteNavState {
	@Immutable
	data object NavigateToBack : CompleteNavState

	@Immutable
	data object NavigateToMain : CompleteNavState
}
