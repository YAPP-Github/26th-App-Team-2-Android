package com.yapp.breake.presentation.onboarding.complete.model

import androidx.compose.runtime.Immutable
import com.yapp.breake.presentation.onboarding.guide.model.GuideEffect

interface CompleteEffect {
	@Immutable
	data object NavigateToBack : GuideEffect

	@Immutable
	data object NavigateToMain : GuideEffect
}
