package com.teambrake.brake.presentation.home.navEntry

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.home.HomeRoute

fun EntryProviderScope<NavKey>.homeNavEntry(padding: PaddingValues) {
	entry<MainTabRoute.Home> {
		HomeRoute(padding = padding)
	}
}
