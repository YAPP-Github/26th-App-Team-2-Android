package com.teambrake.brake.core.navigation.action

import androidx.navigation.NavOptions

interface NavigatorAction {
	fun popBackStack(navOptions: NavOptions? = null)
	fun navigateToLogin(navOptions: NavOptions? = null)
	fun navigateToSignup(navOptions: NavOptions? = null)
	fun navigateToGuide(navOptions: NavOptions? = null)
	fun navigateToPrivacy(navOptions: NavOptions? = null)
	fun navigateToTerms(navOptions: NavOptions? = null)
	fun navigateToComplete(navOptions: NavOptions? = null)
	fun navigateToPermission(navOptions: NavOptions? = null)
	fun navigateToHome(navOptions: NavOptions? = null)
	fun navigateToRegistry(groupId: Long?, navOptions: NavOptions? = null)
	fun navigateToNickname(navOptions: NavOptions? = null)
	fun navigateToOpinion(navOptions: NavOptions? = null)
	fun navigateToInquiry(navOptions: NavOptions? = null)
}
