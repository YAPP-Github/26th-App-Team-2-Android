package com.teambrake.brake.presentation.permission.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.presentation.permission.PermissionRoute
import com.teambrake.brake.core.navigation.route.InitialRoute

fun NavController.navigateToPermission(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Permission, navOptions)
}

fun NavGraphBuilder.permissionNavGraph() {
	composable<InitialRoute.Permission> {
		PermissionRoute()
	}
}
