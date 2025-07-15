package com.yapp.breake.presentation.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.presentation.login.LoginRoute

fun NavController.navigateLogin(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Login, navOptions)
}

fun NavGraphBuilder.loginNavGraph() {
	composable<InitialRoute.Login> {
		LoginRoute()
	}
}
