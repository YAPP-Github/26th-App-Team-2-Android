package com.teambrake.brake.presentation.signup.navEntry

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.teambrake.brake.core.navigation.route.InitialRoute
import com.teambrake.brake.presentation.signup.SignupRoute

fun EntryProviderScope<NavKey>.signupNavEntry() {
	entry<InitialRoute.SignUp> {
		SignupRoute()
	}
}
