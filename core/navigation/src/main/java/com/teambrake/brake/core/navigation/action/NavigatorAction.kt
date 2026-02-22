package com.teambrake.brake.core.navigation.action

interface NavigatorAction {
	fun popBackStack()
	fun navigateToLogin(clearBackStack: Boolean = false)
	fun navigateToSignup(clearBackStack: Boolean = false)
	fun navigateToGuide(clearBackStack: Boolean = false)
	fun navigateToPrivacy(clearBackStack: Boolean = false)
	fun navigateToTerms(clearBackStack: Boolean = false)
	fun navigateToComplete(clearBackStack: Boolean = false)
	fun navigateToPermission(clearBackStack: Boolean = false)
	fun navigateToHome(clearBackStack: Boolean = false)
	fun navigateToRegistry(groupId: Long? = null, clearBackStack: Boolean = false)
	fun navigateToNickname(clearBackStack: Boolean = false)
	fun navigateToOpinion(clearBackStack: Boolean = false)
	fun navigateToInquiry(clearBackStack: Boolean = false)
}
