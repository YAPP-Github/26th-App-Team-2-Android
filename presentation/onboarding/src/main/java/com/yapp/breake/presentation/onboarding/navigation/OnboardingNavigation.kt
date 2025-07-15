package com.yapp.breake.presentation.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.presentation.onboarding.OnboardingRoute

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Onboarding, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph() {
	composable<InitialRoute.Onboarding> {
		OnboardingRoute()
	}
}
