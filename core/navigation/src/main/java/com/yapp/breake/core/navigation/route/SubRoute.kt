package com.yapp.breake.core.navigation.route

import kotlinx.serialization.Serializable

interface SubRoute : Route {
	@Serializable
	data class Registry(val groupId: Long? = null) : SubRoute

	@Serializable
	data object Nickname : SubRoute

	@Serializable
	data object Privacy : SubRoute

	@Serializable
	data object Terms : SubRoute

	sealed interface Feedback : SubRoute {
		@Serializable
		data object Inquiry : Feedback

		@Serializable
		data object Opinion : Feedback
	}
}
