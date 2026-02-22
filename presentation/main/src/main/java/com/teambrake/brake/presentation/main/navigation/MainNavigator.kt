package com.teambrake.brake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.navigation.action.NavigatorAction
import com.teambrake.brake.core.navigation.provider.NavigatorProvider
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.core.navigation.route.RouteStack
import com.teambrake.brake.presentation.feeback.inquiry.navigation.navigateToInquiry
import com.teambrake.brake.presentation.feeback.opinion.navigation.navigateToOpinion
import com.teambrake.brake.presentation.home.navigation.navigateToHome
import com.teambrake.brake.presentation.legal.navigation.navigateToPrivacy
import com.teambrake.brake.presentation.legal.navigation.navigateToTerms
import com.teambrake.brake.presentation.login.navigation.navigateToLogin
import com.teambrake.brake.presentation.nickname.navigation.navigateToNickname
import com.teambrake.brake.presentation.onboarding.navigation.navigateToComplete
import com.teambrake.brake.presentation.onboarding.navigation.navigateToGuide
import com.teambrake.brake.presentation.permission.navigation.navigateToPermission
import com.teambrake.brake.presentation.registry.navigation.navigateToRegistry
import com.teambrake.brake.presentation.report.navigation.navigateReport
import com.teambrake.brake.presentation.setting.navigation.navigateSetting
import com.teambrake.brake.presentation.signup.navigation.navigateToSignup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class MainNavigator(
	val startDestination: Route,
	val navController: NavHostController,
	private val firebaseAnalytics: FirebaseAnalytics,
) {
	private val _routeStack = MutableStateFlow(RouteStack())
	val routeStack: StateFlow<RouteStack> = _routeStack.asStateFlow()

	fun navigatorAction(): NavigatorAction = object : NavigatorAction {
		override fun popBackStack(navOptions: NavOptions?) = popBackStackIfNotHome()
		override fun navigateToLogin(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
			}
			navController.navigateToLogin(navOptions)
		}

		override fun navigateToSignup(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "signup_screen")
			}
			navController.navigateToSignup(navOptions)
		}

		override fun navigateToGuide(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "onboarding_guide_screen")
			}
			navController.navigateToGuide(navOptions)
		}

		override fun navigateToPrivacy(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "privacy_policy_chrome_activity")
			}
			navController.navigateToPrivacy(navOptions)
		}

		override fun navigateToTerms(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "terms_of_service_chrome_activity")
			}
			navController.navigateToTerms(navOptions)
		}

		override fun navigateToComplete(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "onboarding_complete_screen")
			}
			navController.navigateToComplete(navOptions)
		}

		override fun navigateToPermission(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "permission_screen")
			}
			navController.navigateToPermission(navOptions)
		}

		override fun navigateToHome(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "home_screen")
			}
			navController.navigateToHome(navOptions)
		}

		override fun navigateToRegistry(groupId: Long?, navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "registry_screen")
			}
			navController.navigateToRegistry(groupId, navOptions)
		}

		override fun navigateToNickname(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "nickname_screen")
			}
			navController.navigateToNickname(navOptions)
		}

		override fun navigateToOpinion(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "opinion_chrome_screen")
			}
			navController.navigateToOpinion(navOptions)
		}

		override fun navigateToInquiry(navOptions: NavOptions?) {
			firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
				param(FirebaseAnalytics.Param.SCREEN_NAME, "inquiry_chrome_screen")
			}
			navController.navigateToInquiry(navOptions)
		}
	}

	fun navigatorProvider(): NavigatorProvider = object : NavigatorProvider {
		override fun getNavOptionsClearingBackStack(): NavOptions = navOptions {
			popUpTo(navController.graph.id) {
				inclusive = true
			}
			launchSingleTop = true
		}

		override fun getPreviousDestination(): Route? = routeStack.value.previous
	}

	fun navigate(tab: MainTab) {
		val topNavOptions = navOptions {
			popUpTo(navController.graph.id) {
				inclusive = true
			}
			launchSingleTop = true
		}
		when (tab) {
			MainTab.REPORT -> {
				firebaseAnalytics.apply {
					logEvent("bottom_navigation_click") {
						param("name", "report_screen")
					}
					logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
						param(FirebaseAnalytics.Param.SCREEN_NAME, "report_screen")
					}
				}
				navController.navigateReport(navOptions = topNavOptions)
			}
			MainTab.HOME -> {
				firebaseAnalytics.apply {
					logEvent("bottom_navigation_click") {
						param("name", "home_screen")
					}
					logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
						param(FirebaseAnalytics.Param.SCREEN_NAME, "home_screen")
					}
				}
				navController.navigateToHome(navOptions = topNavOptions)
			}
			MainTab.SETTING -> {
				firebaseAnalytics.apply {
					logEvent("bottom_navigation_click") {
						param("name", "setting_screen")
					}
					logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
						param(FirebaseAnalytics.Param.SCREEN_NAME, "setting_screen")
					}
				}
				navController.navigateSetting(navOptions = topNavOptions)
			}
		}
	}

	private fun popBackStackIfNotHome() {
		if (!isSameCurrentDestination<MainTabRoute.Home>()) {
			navController.popBackStack()
		}
	}

	private inline fun <reified T : Route> isSameCurrentDestination(): Boolean = navController.currentDestination?.hasRoute<T>() == true
}

@Composable
internal fun rememberMainNavigator(
	startDestination: Route,
	navController: NavHostController = rememberNavController(),
): MainNavigator {
	val context = LocalContext.current
	val analytics = FirebaseAnalytics.getInstance(context)
	return remember(navController) {
		MainNavigator(startDestination, navController, analytics)
	}
}
