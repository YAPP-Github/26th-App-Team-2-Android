package com.teambrake.brake.presentation.permission.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.presentation.permission.PermissionRoute

fun EntryProviderScope<NavKey>.permissionNavEntry() {
	entry<InitialRoute.Permission> {
		PermissionRoute()
	}
}
