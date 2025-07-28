package com.yapp.breake.core.navigation.action

import androidx.navigation.NavOptions

interface NavigatorAction {
	fun getNavOptionsClearingBackStack(): NavOptions
	fun popBackStack(navOptions: NavOptions? = null)
	fun navigateToLogin(navOptions: NavOptions? = null)
	fun navigateToSignup(navOptions: NavOptions? = null)
	fun navigateToGuide(navOptions: NavOptions? = null)
	fun navigateToComplete(navOptions: NavOptions? = null)
	fun navigateToPermission(navOptions: NavOptions? = null)
	fun navigateToHome(navOptions: NavOptions? = null)
}
