package com.teambrake.brake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreFeedback(
	val sessionCount: Int = 0,
	val popupShown: Boolean = false,
) {
	companion object {
		val Default = DatastoreFeedback()
	}
}
