package com.teambrake.brake.core.datastore.model

import kotlinx.serialization.Serializable

@Serializable
data class DatastoreOnboarding(val flag: Boolean) {
	companion object {
		val Default = DatastoreOnboarding(flag = false)
	}
}
