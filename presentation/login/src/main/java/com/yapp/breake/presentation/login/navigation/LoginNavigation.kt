package com.yapp.breake.presentation.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.InitialRoute
import com.yapp.breake.presentation.login.LoginRoute

fun NavController.navigateLogin(navOptions: NavOptions) {
	navigate(InitialRoute.Login, navOptions)
}

fun NavGraphBuilder.homeNavGraph(
	navigateToSignup: () -> Unit,
	navigateToOnboarding: () -> Unit,
	navigateToHome: () -> Unit,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
) {
	composable<InitialRoute.Login> {
		LoginRoute(
			navigateToSignup = navigateToSignup,
			navigateToOnboarding = navigateToOnboarding,
			navigateToHome = navigateToHome,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
