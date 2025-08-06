package com.yapp.breake.presentation.permission.model

import androidx.compose.runtime.Immutable

sealed interface PermissionModalState {
	@Immutable
	data object PermissionIdle : PermissionModalState

	@Immutable
	data object ShowLogoutModal : PermissionModalState

	@Immutable
	data object ShowAccessibilityAgreementModal : PermissionModalState
}
