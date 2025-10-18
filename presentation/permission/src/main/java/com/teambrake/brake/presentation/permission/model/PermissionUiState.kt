package com.teambrake.brake.presentation.permission.model

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.PersistentList

@Immutable
data class PermissionUiState(
	val permissions: PersistentList<PermissionItem>,
)
