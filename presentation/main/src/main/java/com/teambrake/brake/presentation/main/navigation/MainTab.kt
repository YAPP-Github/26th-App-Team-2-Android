package com.teambrake.brake.presentation.main.navigation

import androidx.compose.runtime.Composable
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.core.navigation.route.Route
import com.teambrake.brake.presentation.main.R

internal enum class MainTab(
	val iconResId: Int,
	internal val contentDescription: Int,
	val route: MainTabRoute,
) {
	REPORT(
		iconResId = R.drawable.ic_chart,
		contentDescription = R.string.tab_report,
		MainTabRoute.Report,
	),
	HOME(
		iconResId = R.drawable.ic_timer,
		contentDescription = R.string.tab_home,
		MainTabRoute.Home,
	),
	SETTING(
		iconResId = R.drawable.ic_user,
		contentDescription = R.string.tab_setting,
		MainTabRoute.Setting,
	),
	;

	companion object {
		@Composable
		fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? = entries.find { predicate(it.route) }

		@Composable
		fun contains(predicate: @Composable (Route) -> Boolean): Boolean = entries.map { it.route }.any { predicate(it) }
	}
}
