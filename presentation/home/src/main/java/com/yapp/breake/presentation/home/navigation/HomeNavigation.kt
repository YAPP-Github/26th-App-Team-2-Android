package com.yapp.breake.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.presentation.home.HomeRoute

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
