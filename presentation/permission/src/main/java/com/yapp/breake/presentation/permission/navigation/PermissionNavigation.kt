package com.yapp.breake.presentation.permission.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.route.InitialRoute
import com.yapp.breake.presentation.permission.PermissionRoute

fun NavController.navigateToPermission(navOptions: NavOptions? = null) {
	navigate(InitialRoute.Permission, navOptions)
}

fun NavGraphBuilder.permissionNavGraph() {
	composable<InitialRoute.Permission> {
		PermissionRoute()
	}
}
