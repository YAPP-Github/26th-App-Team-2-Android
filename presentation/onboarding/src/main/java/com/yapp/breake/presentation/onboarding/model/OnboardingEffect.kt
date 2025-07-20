package com.yapp.breake.presentation.onboarding.model

import android.content.Intent
import androidx.compose.runtime.Immutable

interface OnboardingEffect {

	@Immutable
	data object NavigateToBack : OnboardingEffect

	@Immutable
	data class RequestPermission(
		val intent: Intent,
	) : OnboardingEffect

	@Immutable
	data object NavigateToMain : OnboardingEffect
}
