package com.yapp.breake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.InitialRoute
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.core.navigation.Route
import com.yapp.breake.presentation.home.navigation.navigateHome
import com.yapp.breake.presentation.report.navigation.navigateReport
import com.yapp.breake.presentation.setting.navigation.navigateSetting

internal class MainNavigator(
	val navController: NavHostController,
) {
	private val currentDestination: NavDestination?
		@Composable get() = navController.currentBackStackEntryAsState().value?.destination

	val startDestination = InitialRoute.Login

	val currentTab: MainTab?
		@Composable get() = MainTab.Companion.find { tab ->
			currentDestination?.hasRoute(tab::class) == true
		}

	fun navigate(tab: MainTab) {
		val topNavOptions = navOptions {
			popUpTo(navController.graph.findStartDestination().id) {
				saveState = true
			}
			launchSingleTop = true
			restoreState = true
		}
		when (tab) {
			MainTab.REPORT -> navController.navigateReport(navOptions = topNavOptions)
			MainTab.HOME -> navController.navigateHome(navOptions = topNavOptions)
			MainTab.SETTING -> navController.navigateSetting(navOptions = topNavOptions)
		}
	}

	private fun popBackStack() {
		navController.popBackStack()
	}

	fun popBackStackIfNotHome() {
		if (!isSameCurrentDestination<MainTabRoute.Home>()) {
			popBackStack()
		}
	}

	private inline fun <reified T : Route> isSameCurrentDestination(): Boolean {
		return navController.currentDestination?.hasRoute<T>() == true
	}

	@Composable
	fun shouldShowBottomBar() = MainTab.Companion.contains {
		currentDestination?.hasRoute(it::class) == true
	}
}

@Composable
internal fun rememberMainNavigator(
	navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
	MainNavigator(navController)
}
