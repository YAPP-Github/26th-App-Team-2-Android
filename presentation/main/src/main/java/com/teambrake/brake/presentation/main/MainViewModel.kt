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
		firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
			param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
		}
		viewModelScope.launch {
			val result = decideStartDestinationUseCase()
			val route = when (result) {
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
			_startRoute.value = route
		}
	}

	private fun onDecideDestinationSuccess(destination: Destination, context: Context): Route =
		when (destination) {
			is Destination.Login -> {
				firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
					param(FirebaseAnalytics.Param.SCREEN_NAME, "login_screen")
				}
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
}
