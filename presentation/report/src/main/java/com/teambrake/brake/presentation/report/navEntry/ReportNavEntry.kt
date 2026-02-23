package com.teambrake.brake.presentation.report.navEntry

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.MainTabRoute
import com.teambrake.brake.presentation.report.ReportRoute

fun EntryProviderScope<NavKey>.reportNavEntry(
	padding: PaddingValues,
) {
	entry<MainTabRoute.Report> {
		ReportRoute(padding = padding)
	}
}
