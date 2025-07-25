package com.yapp.breake.presentation.onboarding.guide.model

import androidx.compose.runtime.Immutable

interface GuideEffect {

	@Immutable
	data object NavigateToBack : GuideEffect

	@Immutable
	data object NavigateToPermission : GuideEffect

	@Immutable
	data object NavigateToComplete : GuideEffect
}
