package com.teambrake.brake.presentation.onboarding.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.presentation.onboarding.complete.CompleteRoute
import com.teambrake.brake.presentation.onboarding.guide.GuideRoute

fun EntryProviderScope<NavKey>.onboardingNavEntries() {
	entry<InitialRoute.Onboarding.Guide> {
		GuideRoute()
	}
	entry<InitialRoute.Onboarding.Complete> {
		CompleteRoute()
	}
}
