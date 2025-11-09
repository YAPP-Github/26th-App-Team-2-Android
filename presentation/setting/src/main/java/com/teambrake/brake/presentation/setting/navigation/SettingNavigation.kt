package com.teambrake.brake.presentation.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.setting.SettingRoute

fun NavController.navigateSetting(
	shouldClearBackstack: Boolean = false,
	navOptions: NavOptions? = null,
) {
	navigate(
		route = MainTabRoute.Setting,
		navOptions = navOptions {
			if (shouldClearBackstack) {
				popUpTo(graph.id) { inclusive = true }
			}
			navOptions
		},
	)
}

fun NavGraphBuilder.settingNavGraph(
	padding: PaddingValues,
	onChangeDarkTheme: (Boolean) -> Unit,
) {
	composable<MainTabRoute.Setting> {
		SettingRoute(
			paddingValue = padding,
			onChangeDarkTheme = onChangeDarkTheme,
		)
	}
}
