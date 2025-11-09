package com.teambrake.brake.presentation.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.presentation.onboarding.complete.CompleteRoute
import com.teambrake.brake.presentation.onboarding.guide.GuideRoute
import com.teambrake.brake.core.navigation.route.InitialRoute

fun NavController.navigateToGuide(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Onboarding.Guide, navOptions)
}

fun NavController.navigateToComplete(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Onboarding.Complete, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph() {
	composable<InitialRoute.Onboarding.Guide> {
		GuideRoute()
	}
	composable<InitialRoute.Onboarding.Complete> {
		CompleteRoute()
	}
}
