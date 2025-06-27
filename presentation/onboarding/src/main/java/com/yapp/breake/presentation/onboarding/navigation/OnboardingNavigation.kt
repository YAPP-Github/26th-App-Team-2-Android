package com.yapp.breake.presentation.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.InitialRoute
import com.yapp.breake.presentation.onboarding.OnboardingRoute

fun NavController.navigateOnboarding(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Onboarding, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph(
	navigateToHome: () -> Unit,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
) {
	composable<InitialRoute.Onboarding> {
		OnboardingRoute(
			navigateToHome = navigateToHome,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
