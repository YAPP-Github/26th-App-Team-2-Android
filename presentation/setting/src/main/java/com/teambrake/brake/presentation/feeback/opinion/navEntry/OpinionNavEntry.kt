package com.teambrake.brake.presentation.feeback.opinion.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.feeback.opinion.OpinionRoute

fun EntryProviderScope<NavKey>.opinionNavEntry() {
	entry<SubRoute.Feedback.Opinion> {
		OpinionRoute()
	}
}
