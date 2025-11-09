package com.teambrake.brake.presentation.registry.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.registry.RegistryRoute

fun NavController.navigateToRegistry(
	groupId: Long?,
	navOptions: NavOptions? = null,
) {
	navigate(
		route = SubRoute.Registry(groupId),
		navOptions = navOptions,
	)
}

fun NavGraphBuilder.registryNavGraph() {
	composable<SubRoute.Registry> { backstackEntry ->
		RegistryRoute(viewModel = hiltViewModel(backstackEntry))
	}
}
