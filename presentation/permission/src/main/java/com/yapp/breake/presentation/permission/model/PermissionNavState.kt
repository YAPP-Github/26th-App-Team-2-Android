package com.yapp.breake.presentation.permission.model

import android.content.Intent
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface PermissionNavState {

	@Immutable
	data object NavigateToBack : PermissionNavState

	@Immutable
	data object NavigateToLogin : PermissionNavState

	@Immutable
	data class RequestPermission(
		val intent: Intent,
	) : PermissionNavState

	@Immutable
	data object NavigateToComplete : PermissionNavState

	@Immutable
	data object NavigateToMain : PermissionNavState
}
