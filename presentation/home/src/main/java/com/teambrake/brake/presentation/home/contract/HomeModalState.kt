package com.teambrake.brake.presentation.home.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.teambrake.brake.core.model.app.AppGroup

@Stable
internal sealed interface HomeModalState {

	@Immutable
	data object Nothing : HomeModalState

	@Immutable
	data class StopUsingDialog(val appGroup: AppGroup) : HomeModalState
}
