package com.teambrake.brake.presentation.registry.model

import androidx.compose.runtime.Immutable

sealed interface RegistryNavState {
	@Immutable
	data object NavigateToHome : RegistryNavState
}
