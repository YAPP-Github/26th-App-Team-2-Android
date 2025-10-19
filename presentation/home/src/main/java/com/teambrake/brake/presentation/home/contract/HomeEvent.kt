package com.teambrake.brake.presentation.home.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface HomeEvent {

	@Immutable
	data class ShowStopUsingSuccess(val groupName: String) : HomeEvent

	@Immutable
	data class NavigateToRegistry(val groupId: Long?) : HomeEvent
}
