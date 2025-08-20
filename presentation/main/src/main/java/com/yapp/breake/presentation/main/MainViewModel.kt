package com.yapp.breake.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.breake.core.permission.PermissionManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCase
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
