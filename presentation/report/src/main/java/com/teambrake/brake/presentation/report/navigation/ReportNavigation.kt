package com.teambrake.brake.presentation.report.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.teambrake.brake.presentation.report.ReportRoute
import com.teambrake.brake.core.navigation.route.MainTabRoute

fun NavController.navigateReport(
	shouldClearBackstack: Boolean = false,
	navOptions: NavOptions? = null,
) {
	navigate(
		route = MainTabRoute.Report,
		navOptions = navOptions {
			if (shouldClearBackstack) {
				popUpTo(graph.id) { inclusive = true }
			}
			navOptions
		},
	)
}

fun NavGraphBuilder.reportNavGraph(
	padding: PaddingValues,
) {
	composable<MainTabRoute.Report> {
		ReportRoute(padding = padding)
	}
}
