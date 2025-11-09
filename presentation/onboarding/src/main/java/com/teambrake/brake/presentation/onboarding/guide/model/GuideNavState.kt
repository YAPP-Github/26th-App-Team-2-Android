package com.teambrake.brake.presentation.onboarding.guide.model

import androidx.compose.runtime.Immutable

interface GuideNavState {

	@Immutable
	data object NavigateToLogin : GuideNavState

	@Immutable
	data object NavigateToPermission : GuideNavState

	@Immutable
	data object NavigateToComplete : GuideNavState
}
