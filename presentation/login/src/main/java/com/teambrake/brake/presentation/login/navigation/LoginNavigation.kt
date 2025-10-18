package com.teambrake.brake.presentation.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.presentation.login.LoginRoute

fun NavController.navigateToLogin(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph() {
	composable<InitialRoute.Login> {
		LoginRoute()
	}
}
