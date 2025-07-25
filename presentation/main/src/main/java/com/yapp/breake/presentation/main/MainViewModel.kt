package com.yapp.breake.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.breake.core.permission.PermissionManager
import com.yapp.breake.core.model.user.Destination
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
	private val permissionManager: PermissionManager,
	private val decideStartDestinationUseCase: DecideStartDestinationUseCase,
) : ViewModel() {

	fun decideStartDestination(context: Context): Route {
		val dest = decideStartDestinationUseCase()
		return when (dest) {
			is Destination.Login -> InitialRoute.Login

			is Destination.Onboarding -> InitialRoute.Onboarding.Guide

			is Destination.PermissionOrHome -> {
				if (permissionManager.isAllGranted(context)) {
					MainTabRoute.Home
				} else {
					InitialRoute.Permission
				}
			}
		}
	}
}
