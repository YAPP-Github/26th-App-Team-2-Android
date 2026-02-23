package com.teambrake.brake.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.navigation.action.NavigatorAction
import com.teambrake.brake.core.navigation.provider.NavigatorProvider
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.core.navigation.route.LaunchMode
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.core.navigation.route.RouteStack
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.core.permission.PermissionManager
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.DecideStartDestinationUseCaseError
import com.teambrake.brake.domain.model.result.error.HttpUnsuccessfulCodeError
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
import com.teambrake.brake.domain.model.result.error.RemoteServerNotReachedError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.OfflineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.OnlineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.PreAuthSuccess
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import com.teambrake.brake.presentation.main.navigation.MainTab
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class MainViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val decideStartDestinationUseCase: DecideStartDestinationUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	private val _routeStack = MutableStateFlow(RouteStack())
	val routeStack: StateFlow<RouteStack> = _routeStack.asStateFlow()

	val navigatorAction: NavigatorAction = object : NavigatorAction {
		override fun popBackStack() {
			goBack()
		}

		override fun navigateToLogin(launchMode: LaunchMode) {
			logScreenView("login_screen")
			navigate(InitialRoute.Login, launchMode)
		}

		override fun navigateToSignup(launchMode: LaunchMode) {
			logScreenView("signup_screen")
			navigate(InitialRoute.SignUp, launchMode)
		}

		override fun navigateToGuide(launchMode: LaunchMode) {
			logScreenView("onboarding_guide_screen")
			navigate(InitialRoute.Onboarding.Guide, launchMode)
		}

		override fun navigateToPrivacy(launchMode: LaunchMode) {
			logScreenView("privacy_policy_chrome_activity")
			navigate(SubRoute.Privacy, launchMode)
		}

		override fun navigateToTerms(launchMode: LaunchMode) {
			logScreenView("terms_of_service_chrome_activity")
			navigate(SubRoute.Terms, launchMode)
		}

		override fun navigateToComplete(launchMode: LaunchMode) {
			logScreenView("onboarding_complete_screen")
			navigate(InitialRoute.Onboarding.Complete, launchMode)
		}

		override fun navigateToPermission(launchMode: LaunchMode) {
			logScreenView("permission_screen")
			navigate(InitialRoute.Permission, launchMode)
		}

		override fun navigateToHome(launchMode: LaunchMode) {
			logScreenView("home_screen")
			navigate(MainTabRoute.Home, launchMode)
		}

		override fun navigateToRegistry(groupId: Long?, launchMode: LaunchMode) {
			logScreenView("registry_screen")
			navigate(SubRoute.Registry(groupId), launchMode)
		}

		override fun navigateToNickname(launchMode: LaunchMode) {
			logScreenView("nickname_screen")
			navigate(SubRoute.Nickname, launchMode)
		}

		override fun navigateToOpinion(launchMode: LaunchMode) {
			logScreenView("opinion_chrome_screen")
			navigate(SubRoute.Feedback.Opinion, launchMode)
		}

		override fun navigateToInquiry(launchMode: LaunchMode) {
			logScreenView("inquiry_chrome_screen")
			navigate(SubRoute.Feedback.Inquiry, launchMode)
		}
	}

	val navigatorProvider: NavigatorProvider = object : NavigatorProvider {
		override fun getPreviousDestination(): Route? = routeStack.value.previous
	}

	init {
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "main_activity")
		}
	}

	fun navigate(
		route: Route,
		launchMode: LaunchMode = LaunchMode.STANDARD,
	) {
		_routeStack.update { current ->
			when (launchMode) {
				LaunchMode.CLEAR_ALL -> RouteStack(backStack = listOf(route))
				LaunchMode.SINGLE_TOP -> {
					if (route == current.current) {
						current
					} else {
						current.copy(backStack = current.backStack + route)
					}
				}
				LaunchMode.CLEAR_TOP -> {
					if (route in current.backStack) {
						current.copy(
							backStack = current.backStack.takeWhile { it != route } + route,
						)
					} else {
						current.copy(backStack = current.backStack + route)
					}
				}
				LaunchMode.STANDARD -> current.copy(backStack = current.backStack + route)
			}
		}
	}

	fun goBack() {
		_routeStack.update { current ->
			if (current.backStack.size <= 1) {
				current
			} else {
				current.copy(
					backStack = current.backStack.dropLast(1),
				)
			}
		}
	}

	fun navigateTab(tab: MainTab) {
		when (tab) {
			MainTab.REPORT -> {
				logBottomNavigationClick("report_screen")
				logScreenView("report_screen")
				navigate(MainTabRoute.Report, LaunchMode.CLEAR_ALL)
			}
			MainTab.HOME -> {
				logBottomNavigationClick("home_screen")
				logScreenView("home_screen")
				navigate(MainTabRoute.Home, LaunchMode.CLEAR_ALL)
			}
			MainTab.SETTING -> {
				logBottomNavigationClick("setting_screen")
				logScreenView("setting_screen")
				navigate(MainTabRoute.Setting, LaunchMode.CLEAR_ALL)
			}
		}
	}

	fun decideStartDestination(context: Context) {
		logScreenView("login_screen")
		viewModelScope.launch {
			val route = when (val result = decideStartDestinationUseCase()) {
				is BrakeResult.Success -> {
					val data = when (val success = result.data) {
						is OnlineAuthorizedSuccess -> success.data
						is OfflineAuthorizedSuccess -> success.data
						is PreAuthSuccess -> success.data
					}
					onDecideDestinationSuccess(data, context)
				}

				is BrakeResult.Error -> {
					onDecideDestinationError(result.error)
				}
			}

			_routeStack.update { RouteStack(route) }
		}
	}

	private fun onDecideDestinationSuccess(destination: Destination, context: Context): Route =
		when (destination) {
			is Destination.Login -> {
				logScreenView("login_screen")
				InitialRoute.Login
			}

			is Destination.Onboarding -> InitialRoute.Onboarding.Guide

			is Destination.PermissionOrHome -> {
				if (permissionManager.isAllGranted(context)) {
					MainTabRoute.Home
				} else {
					InitialRoute.Permission
				}
			}

			else -> InitialRoute.Login
		}

	private fun onDecideDestinationError(error: DecideStartDestinationUseCaseError): Route =
		when (error) {
			is HttpUnsuccessfulCodeError, is LocalApiCallError, is RemoteServerNotReachedError, is UndefinedExceptionError -> {
				InitialRoute.Login
			}
		}

	fun analyzeFinishApp() {
		firebaseAnalytics.logEvent("app_exit") {
			param("reason", "user_exit")
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
