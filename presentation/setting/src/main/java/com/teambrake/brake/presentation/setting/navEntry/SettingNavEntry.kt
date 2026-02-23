package com.teambrake.brake.presentation.setting.navEntry

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.setting.SettingRoute

fun EntryProviderScope<NavKey>.settingNavEntry(
	padding: PaddingValues,
	onChangeDarkTheme: (Boolean) -> Unit,
) {
	entry<MainTabRoute.Setting> {
		SettingRoute(
			paddingValue = padding,
			onChangeDarkTheme = onChangeDarkTheme,
		)
	}
}
