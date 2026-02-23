package com.teambrake.brake.presentation.registry.navEntry

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.registry.RegistryRoute
import com.teambrake.brake.presentation.registry.RegistryViewModel

fun EntryProviderScope<NavKey>.registryNavEntry() {
	entry<SubRoute.Registry> { route ->
		RegistryRoute(
			viewModel = hiltViewModel(
				creationCallback = { factory: RegistryViewModel.Factory ->
					factory.create(route.groupId)
				},
			),
		)
	}
}
