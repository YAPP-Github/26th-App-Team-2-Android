package com.teambrake.brake.presentation.registry.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.registry.RegistryRoute

fun EntryProviderScope<NavKey>.registryNavEntry() {
	entry<SubRoute.Registry> {
		RegistryRoute()
	}
}
