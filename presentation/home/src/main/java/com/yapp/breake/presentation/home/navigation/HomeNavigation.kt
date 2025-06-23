package com.yapp.breake.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.presentation.home.HomeRoute

fun NavController.navigateHome(
	shouldClearBackstack: Boolean = false,
	navOptions: NavOptions? = null
) {
	navigate(
		route = MainTabRoute.Home,
		navOptions = navOptions {
			if (shouldClearBackstack) {
				popUpTo(graph.id) { inclusive = true }
			}
			navOptions
		},
	)
}

fun NavGraphBuilder.homeNavGraph(
	padding: PaddingValues,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
) {
	composable<MainTabRoute.Home> {
		HomeRoute(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
