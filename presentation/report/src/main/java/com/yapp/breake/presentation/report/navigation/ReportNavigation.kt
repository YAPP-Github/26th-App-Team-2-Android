package com.yapp.breake.presentation.report.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.presentation.report.ReportRoute

fun NavController.navigateReport(
	shouldClearBackstack: Boolean = false,
	navOptions: NavOptions? = null
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
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
) {
	composable<MainTabRoute.Report> {
		ReportRoute(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
