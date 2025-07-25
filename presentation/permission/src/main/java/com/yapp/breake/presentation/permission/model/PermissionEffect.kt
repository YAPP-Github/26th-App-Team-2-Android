package com.yapp.breake.presentation.permission.model

import android.content.Intent
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
interface PermissionEffect {

	@Immutable
	data object NavigateToBack : PermissionEffect

	@Immutable
	data class RequestPermission(
		val intent: Intent,
	) : PermissionEffect

	@Immutable
	data object NavigateToComplete : PermissionEffect

	@Immutable
	data object NavigateToMain : PermissionEffect
}
