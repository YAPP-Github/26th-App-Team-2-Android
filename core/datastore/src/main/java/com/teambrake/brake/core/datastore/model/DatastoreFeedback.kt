package com.teambrake.brake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreFeedback(
	val sessionCount: Int = 0,
	val status: String = "",
	val lastShownAt: Long = 0L,
) {
	companion object {
		val Default = DatastoreFeedback()
	}
}
