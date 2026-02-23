package com.teambrake.brake.presentation.login.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.presentation.login.LoginRoute

fun EntryProviderScope<NavKey>.loginNavEntry() {
	entry<InitialRoute.Login> {
		LoginRoute()
	}
}
