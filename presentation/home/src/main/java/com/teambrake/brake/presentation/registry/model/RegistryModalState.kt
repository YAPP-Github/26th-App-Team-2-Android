package com.teambrake.brake.presentation.registry.model

import androidx.compose.runtime.Immutable

sealed interface RegistryModalState {
	@Immutable
	data object Idle : RegistryModalState

	@Immutable
	data object ShowGroupDeletionWarning : RegistryModalState
}
