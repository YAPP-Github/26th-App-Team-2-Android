package com.yapp.breake.core.navigation

import kotlinx.serialization.Serializable

sealed interface InitialRoute : Route {
	@Serializable
	data object Login : InitialRoute

	@Serializable
	data object SignUp : InitialRoute

	@Serializable
	data object Onboarding : InitialRoute
}
