package com.teambrake.brake.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface InitialRoute : Route {
	@Serializable
	data object Login : InitialRoute

	@Serializable
	data object SignUp : InitialRoute

	@Serializable
	sealed interface Onboarding : InitialRoute {
		@Serializable
		data object Guide : Onboarding

		@Serializable
		data object Complete : Onboarding
	}

	@Serializable
	data object Permission : InitialRoute
}
