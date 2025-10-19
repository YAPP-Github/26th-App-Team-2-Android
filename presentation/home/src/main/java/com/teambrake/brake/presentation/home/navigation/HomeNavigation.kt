package com.teambrake.brake.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.home.HomeRoute

fun NavController.navigateToHome(
	navOptions: NavOptions? = null,
) {
	navigate(
		route = MainTabRoute.Home,
		navOptions = navOptions,
	)
}

fun NavGraphBuilder.homeNavGraph(padding: PaddingValues) {
	composable<MainTabRoute.Home> {
		HomeRoute(padding = padding)
	}
}
