package com.teambrake.brake.presentation.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.navigation.action.NavigatorAction
import com.teambrake.brake.core.navigation.provider.NavigatorProvider
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.core.navigation.route.RouteStack
import com.teambrake.brake.core.navigation.route.SubRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class MainNavigator(
	val startDestination: Route,
	private val firebaseAnalytics: FirebaseAnalytics,
) {
	private val _routeStack = MutableStateFlow(RouteStack())
	val routeStack: StateFlow<RouteStack> = _routeStack.asStateFlow()

	fun navigate(route: Route, clearBackStack: Boolean = false) {
		if (clearBackStack) {
			_routeStack.update {
				RouteStack(stack = listOf(route))
			}
		} else {
			_routeStack.update { current ->
				if (route in current.stack) {
					current.copy(
						stack = current.stack.takeWhile { it != route } + route
					)
				} else {
					current.copy(
						stack = current.stack + route
					)
				}
			}
		}
	}

	fun goBack() {
		_routeStack.update { current ->
			if (current.stack.size <= 1) {
				current
			} else {
				current.copy(
					stack = current.stack.dropLast(1)
				)
			}
		}
	}

	fun navigatorAction(): NavigatorAction = object : NavigatorAction {
		override fun popBackStack() {
			goBack()
		}

		override fun navigateToLogin(clearBackStack: Boolean) {
			logScreenView("login_screen")
			navigate(InitialRoute.Login, clearBackStack)
		}

		override fun navigateToSignup(clearBackStack: Boolean) {
			logScreenView("signup_screen")
			navigate(InitialRoute.SignUp, clearBackStack)
		}

		override fun navigateToGuide(clearBackStack: Boolean) {
			logScreenView("onboarding_guide_screen")
			navigate(InitialRoute.Onboarding.Guide, clearBackStack)
		}

		override fun navigateToPrivacy(clearBackStack: Boolean) {
			logScreenView("privacy_policy_chrome_activity")
			navigate(SubRoute.Privacy, clearBackStack)
		}

		override fun navigateToTerms(clearBackStack: Boolean) {
			logScreenView("terms_of_service_chrome_activity")
			navigate(SubRoute.Terms, clearBackStack)
		}

		override fun navigateToComplete(clearBackStack: Boolean) {
			logScreenView("onboarding_complete_screen")
			navigate(InitialRoute.Onboarding.Complete, clearBackStack)
		}

		override fun navigateToPermission(clearBackStack: Boolean) {
			logScreenView("permission_screen")
			navigate(InitialRoute.Permission, clearBackStack)
		}

		override fun navigateToHome(clearBackStack: Boolean) {
			logScreenView("home_screen")
			navigate(MainTabRoute.Home, clearBackStack)
		}

		override fun navigateToRegistry(groupId: Long?, clearBackStack: Boolean) {
			logScreenView("registry_screen")
			navigate(SubRoute.Registry(groupId), clearBackStack)
		}

		override fun navigateToNickname(clearBackStack: Boolean) {
			logScreenView("nickname_screen")
			navigate(SubRoute.Nickname, clearBackStack)
		}

		override fun navigateToOpinion(clearBackStack: Boolean) {
			logScreenView("opinion_chrome_screen")
			navigate(SubRoute.Feedback.Opinion, clearBackStack)
		}

		override fun navigateToInquiry(clearBackStack: Boolean) {
			logScreenView("inquiry_chrome_screen")
			navigate(SubRoute.Feedback.Inquiry, clearBackStack)
		}
	}

	fun navigatorProvider(): NavigatorProvider = object : NavigatorProvider {
		override fun getPreviousDestination(): Route? = routeStack.value.previous
	}

	fun navigateTab(tab: MainTab) {
		when (tab) {
			MainTab.REPORT -> {
				logBottomNavigationClick("report_screen")
				logScreenView("report_screen")
				navigate(MainTabRoute.Report, true)
			}
			MainTab.HOME -> {
				logBottomNavigationClick("home_screen")
				logScreenView("home_screen")
				navigate(MainTabRoute.Home, true)
			}
			MainTab.SETTING -> {
				logBottomNavigationClick("setting_screen")
				logScreenView("setting_screen")
				navigate(MainTabRoute.Setting, true)
			}
		}
	}

	private fun logScreenView(screenName: String) {
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
		}
	}

	private fun logBottomNavigationClick(screenName: String) {
		firebaseAnalytics.logEvent("bottom_navigation_click") {
			param("name", screenName)
		}
	}
}

@Composable
internal fun rememberMainNavigator(
	startDestination: Route,
): MainNavigator {
	val context = LocalContext.current
	val analytics = FirebaseAnalytics.getInstance(context)
	return remember {
		MainNavigator(startDestination, analytics)
	}
}
