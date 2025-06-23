package com.yapp.breake.presentation.home.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.presentation.home.HomeRoute

fun NavController.navigateHome(navOptions: NavOptions) {
	navigate(MainTabRoute.Home, navOptions)
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
