package com.teambrake.brake.core.navigation.action

import com.teambrake.brake.core.navigation.route.LaunchMode

interface NavigatorAction {
	fun popBackStack()
	fun navigateToLogin(launchMode: LaunchMode = LaunchMode.CLEAR_ALL)
	fun navigateToSignup(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToGuide(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToPrivacy(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToTerms(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToComplete(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToPermission(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToHome(launchMode: LaunchMode = LaunchMode.CLEAR_ALL)
	fun navigateToRegistry(groupId: Long? = null, launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToNickname(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToOpinion(launchMode: LaunchMode = LaunchMode.STANDARD)
	fun navigateToInquiry(launchMode: LaunchMode = LaunchMode.STANDARD)
}
