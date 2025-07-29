package com.yapp.breake.presentation.registry.model

import androidx.compose.runtime.Immutable

sealed interface RegistryEffect {
	@Immutable
	data object NavigateToHome : RegistryEffect
}
