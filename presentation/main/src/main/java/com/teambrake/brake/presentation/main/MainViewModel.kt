package com.teambrake.brake.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teambrake.brake.core.permission.PermissionManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.teambrake.brake.core.model.user.Destination
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val decideStartDestinationUseCase: DecideStartDestinationUseCase,
	private val firebaseAnalytics: FirebaseAnalytics,
) : ViewModel() {

	private var _startRoute: MutableStateFlow<Route?> = MutableStateFlow<Route?>(null)
	val startRoute: StateFlow<Route?> = _startRoute.asStateFlow()

	init {
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "main_activity")
		}
	}

	fun decideStartDestination(context: Context) {
		viewModelScope.launch {
			val dest = decideStartDestinationUseCase()
			val route = when (dest) {
				is Destination.Login -> {
					firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
						param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
					}
					InitialRoute.Login
				}

				is Destination.Onboarding -> {
					firebaseAnalytics.run {
						logEvent(FirebaseAnalytics.Event.LOGIN) {
							param(FirebaseAnalytics.Param.METHOD, "auto_login")
						}
						logEvent(FirebaseAnalytics.Event.TUTORIAL_BEGIN, null)
					}
					InitialRoute.Onboarding.Guide
				}

				is Destination.PermissionOrHome -> {
					if (permissionManager.isAllGranted(context)) {
						firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
							param(FirebaseAnalytics.Param.METHOD, "auto_login")
						}
						MainTabRoute.Home
					} else {
						firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN) {
							param(FirebaseAnalytics.Param.METHOD, "auto_login")
						}
						InitialRoute.Permission
					}
				}

				else -> {
					InitialRoute.Login
				}
			}
			_startRoute.value = route
		}
	}

	fun analyzeFinishApp() {
		firebaseAnalytics.logEvent("app_exit") {
			param("reason", "user_exit")
		}
	}
}
