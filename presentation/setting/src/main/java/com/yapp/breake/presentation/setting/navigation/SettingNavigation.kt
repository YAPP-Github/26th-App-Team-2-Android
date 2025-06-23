package com.yapp.breake.presentation.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.yapp.breake.core.navigation.MainTabRoute
import com.yapp.breake.presentation.setting.SettingRoute

fun NavController.navigateSetting(navOptions: NavOptions) {
	navigate(MainTabRoute.Setting, navOptions)
}

fun NavGraphBuilder.settingNavGraph(
	padding: PaddingValues,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
) {
	composable<MainTabRoute.Setting> {
		SettingRoute(
			padding = padding,
			onShowErrorSnackBar = onShowErrorSnackBar,
		)
	}
}
