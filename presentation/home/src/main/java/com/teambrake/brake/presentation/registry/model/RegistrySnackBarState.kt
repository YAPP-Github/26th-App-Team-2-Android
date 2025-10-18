package com.teambrake.brake.presentation.registry.model

import androidx.compose.runtime.Immutable
import com.teambrake.brake.core.ui.UiString

sealed interface RegistrySnackBarState {
	@Immutable
	data class Success(val uiString: UiString) : RegistrySnackBarState

	@Immutable
	data class Error(val uiString: UiString) : RegistrySnackBarState
}
