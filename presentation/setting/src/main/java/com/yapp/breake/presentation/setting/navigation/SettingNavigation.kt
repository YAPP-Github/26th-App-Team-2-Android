package com.yapp.breake.presentation.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.presentation.setting.SettingRoute

fun NavController.navigateSetting(
	shouldClearBackstack: Boolean = false,
	navOptions: NavOptions? = null
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
	onShowErrorSnackBar: (Throwable?) -> Unit = {},
	onChangeDarkTheme: (Boolean) -> Unit
) {
	composable<MainTabRoute.Setting> {
		SettingRoute(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
			onChangeDarkTheme = onChangeDarkTheme,
		)
	}
}
