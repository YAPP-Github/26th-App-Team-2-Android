package com.yapp.breake.presentation.main.navigation

import androidx.compose.runtime.Composable
import com.yapp.breake.core.navigation.route.MainTabRoute
import com.yapp.breake.core.navigation.route.Route
import com.yapp.breake.presentation.main.R

internal enum class MainTab(
	val iconResId: Int,
	internal val contentDescription: String,
	val route: MainTabRoute,
) {
	REPORT(
		iconResId = R.drawable.ic_chart,
		contentDescription = "리포트",
		MainTabRoute.Report,
	),
	HOME(
		iconResId = R.drawable.ic_timer,
		contentDescription = "관리",
		MainTabRoute.Home,
	),
	SETTING(
		iconResId = R.drawable.ic_user,
		contentDescription = "내 정보",
		MainTabRoute.Setting,
	),
	;

	companion object {
		@Composable
		fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
			return entries.find { predicate(it.route) }
		}

		@Composable
		fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
			return entries.map { it.route }.any { predicate(it) }
		}
	}
}
