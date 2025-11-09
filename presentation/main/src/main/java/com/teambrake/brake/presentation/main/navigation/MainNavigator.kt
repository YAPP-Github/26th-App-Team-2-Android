package com.teambrake.brake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.navigation.action.NavigatorAction
import com.teambrake.brake.core.navigation.provider.NavigatorProvider
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.core.navigation.route.stringRoute
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
	// 기존 MainTab 상태 변화의 Composable State Producer 를 State Flow 와 함께 쓰면 recomposition 이 두 번 일어나는 문제가 있어,
	// 하나의 State Producer 로 통합
	private val _currentRoute = MutableStateFlow<Route>(startDestination)
	val currentRoute: StateFlow<Route> = _currentRoute.asStateFlow()

	init {
		// NavController의 destination 변화를 감지
		navController.addOnDestinationChangedListener { _, destination, _ ->
			_currentRoute.value = destination.toBrakeRoute
		}
	}

	private val NavDestination.toBrakeRoute: Route
		get() = when (this.route) {
			MainTabRoute.Home.stringRoute() -> MainTabRoute.Home
			MainTabRoute.Setting.stringRoute() -> MainTabRoute.Setting
			MainTabRoute.Report.stringRoute() -> MainTabRoute.Report
			InitialRoute.Login.stringRoute() -> InitialRoute.Login
			InitialRoute.SignUp.stringRoute() -> InitialRoute.SignUp
			InitialRoute.Onboarding.Guide.stringRoute() -> InitialRoute.Onboarding.Guide
			InitialRoute.Onboarding.Complete.stringRoute() -> InitialRoute.Onboarding.Complete
			InitialRoute.Permission.stringRoute() -> InitialRoute.Permission
			SubRoute.Nickname.stringRoute() -> SubRoute.Nickname
			SubRoute.Privacy.stringRoute() -> SubRoute.Privacy
			SubRoute.Terms.stringRoute() -> SubRoute.Terms
			SubRoute.Feedback.Inquiry.stringRoute() -> SubRoute.Feedback.Inquiry
			SubRoute.Feedback.Opinion.stringRoute() -> SubRoute.Feedback.Opinion
			else -> SubRoute.Registry()
			// Registry 타입은 유일하게 인자를 갖는 Route 의 클래스 이므로, else 분기로 Registry 기본 생성자 반환
		}

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

		override fun getPreviousDestination(): String = navController.previousBackStackEntry?.destination?.route
			?: startDestination.stringRoute()
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
