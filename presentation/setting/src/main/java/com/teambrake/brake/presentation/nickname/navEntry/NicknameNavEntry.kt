package com.teambrake.brake.presentation.nickname.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.SubRoute
import com.teambrake.brake.presentation.nickname.NicknameRoute

fun EntryProviderScope<NavKey>.nicknameNavEntry() {
	entry<SubRoute.Nickname> {
		NicknameRoute()
	}
}
