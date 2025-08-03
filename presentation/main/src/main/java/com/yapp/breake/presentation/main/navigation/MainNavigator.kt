package com.yapp.breake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.action.NavigatorAction
import com.yapp.breake.core.navigation.provider.NavigatorProvider
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.core.navigation.route.stringRoute
import com.yapp.breake.presentation.home.navigation.navigateToHome
import com.yapp.breake.presentation.legal.navigation.navigateToPrivacy
import com.yapp.breake.presentation.legal.navigation.navigateToTerms
import com.yapp.breake.presentation.login.navigation.navigateToLogin
import com.yapp.breake.presentation.nickname.navigation.navigateToNickname
import com.yapp.breake.presentation.onboarding.navigation.navigateToComplete
import com.yapp.breake.presentation.onboarding.navigation.navigateToGuide
import com.yapp.breake.presentation.permission.navigation.navigateToPermission
import com.yapp.breake.presentation.registry.navigation.navigateToRegistry
import com.yapp.breake.presentation.report.navigation.navigateReport
import com.yapp.breake.presentation.setting.navigation.navigateSetting
import com.yapp.breake.presentation.signup.navigation.navigateToSignup

internal class MainNavigator(
	val startDestination: Route,
	val navController: NavHostController,
) {
	private val currentDestination: NavDestination?
		@Composable get() = navController.currentBackStackEntryAsState().value?.destination

	val currentTab: MainTab?
		@Composable get() = MainTab.Companion.find { tab ->
			currentDestination?.hasRoute(tab::class) == true
		}

	fun navigatorAction(): NavigatorAction {
		return object : NavigatorAction {
			override fun popBackStack(navOptions: NavOptions?) = popBackStackIfNotHome()
			override fun navigateToLogin(navOptions: NavOptions?) =
				navController.navigateToLogin(navOptions)

			override fun navigateToSignup(navOptions: NavOptions?) =
				navController.navigateToSignup(navOptions)

			override fun navigateToGuide(navOptions: NavOptions?) =
				navController.navigateToGuide(navOptions)

			override fun navigateToPrivacy(navOptions: NavOptions?) {
				navController.navigateToPrivacy(navOptions)
			}

			override fun navigateToTerms(navOptions: NavOptions?) {
				navController.navigateToTerms(navOptions)
			}

			override fun navigateToComplete(navOptions: NavOptions?) =
				navController.navigateToComplete(navOptions)

			override fun navigateToPermission(navOptions: NavOptions?) =
				navController.navigateToPermission(navOptions)

			override fun navigateToHome(navOptions: NavOptions?) =
				navController.navigateToHome(navOptions)

			override fun navigateToRegistry(groupId: Long?, navOptions: NavOptions?) {
				navController.navigateToRegistry(groupId, navOptions)
			}

			override fun navigateToNickname(navOptions: NavOptions?) {
				navController.navigateToNickname(navOptions)
			}
		}
	}

	fun navigatorProvider(): NavigatorProvider {
		return object : NavigatorProvider {
			override fun getNavOptionsClearingBackStack(): NavOptions {
				return navOptions {
					popUpTo(navController.graph.id) {
						inclusive = true
					}
					launchSingleTop = true
				}
			}

			override fun getPreviousDestination(): String {
				return navController.previousBackStackEntry?.destination?.route
					?: startDestination.stringRoute()
			}
		}
	}

	fun navigate(tab: MainTab) {
		val topNavOptions = navOptions {
			popUpTo(navController.graph.id) {
				inclusive = true
			}
			launchSingleTop = true
		}
		when (tab) {
			MainTab.REPORT -> navController.navigateReport(navOptions = topNavOptions)
			MainTab.HOME -> navController.navigateToHome(navOptions = topNavOptions)
			MainTab.SETTING -> navController.navigateSetting(navOptions = topNavOptions)
		}
	}

	private fun popBackStackIfNotHome() {
		if (!isSameCurrentDestination<MainTabRoute.Home>()) {
			navController.popBackStack()
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
	startDestination: Route,
	navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
	MainNavigator(startDestination, navController)
}
