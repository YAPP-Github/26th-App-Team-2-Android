package com.yapp.breake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.navigation.action.NavigatorAction
import com.yapp.breake.core.navigation.provider.NavigatorProvider
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.core.navigation.route.stringRoute
import com.yapp.breake.presentation.feeback.inquiry.navigation.navigateToInquiry
import com.yapp.breake.presentation.feeback.opinion.navigation.navigateToOpinion
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
	private val firebaseAnalytics: FirebaseAnalytics,
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
): MainNavigator {
	val context = LocalContext.current
	val analytics = FirebaseAnalytics.getInstance(context)
	return remember(navController) {
		MainNavigator(startDestination, navController, analytics)
	}
}
