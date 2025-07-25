package com.yapp.breake.presentation.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.presentation.onboarding.complete.CompleteRoute
import com.yapp.breake.presentation.onboarding.guide.GuideRoute

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
