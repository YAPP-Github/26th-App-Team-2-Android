package com.yapp.breake.presentation.onboarding.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.PersistentList

@Stable
sealed interface OnboardingUiState {

	@Immutable
	data class Guide(val startPage: Int) : OnboardingUiState

	@Immutable
	data class Permission(
		val permissions: PersistentList<PermissionItem>,
	) : OnboardingUiState

	@Immutable
	data object Complete : OnboardingUiState
}
