package com.teambrake.brake.presentation.onboarding.guide.model

import androidx.compose.runtime.Immutable

sealed interface GuideModalState {
	@Immutable
	data object GuideIdle : GuideModalState

	@Immutable
	data object ShowLogoutModal : GuideModalState
}
