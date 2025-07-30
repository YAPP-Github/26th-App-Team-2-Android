package com.yapp.breake.core.navigation.route

import kotlinx.serialization.Serializable

interface SubRoute : Route {
	@Serializable
	data class Registry(val groupId: Long? = null) : SubRoute

	@Serializable
	data object Nickname : SubRoute
}
