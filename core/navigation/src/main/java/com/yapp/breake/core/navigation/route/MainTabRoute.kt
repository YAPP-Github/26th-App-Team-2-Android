package com.yapp.breake.core.navigation.route

import kotlinx.serialization.Serializable

sealed interface MainTabRoute : Route {

	@Serializable
	data object Report : MainTabRoute

	@Serializable
	data object Home : MainTabRoute

	@Serializable
	data object Setting : MainTabRoute
}
