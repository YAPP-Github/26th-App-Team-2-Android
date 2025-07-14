package com.yapp.breake.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(
	val nickname: String,
	val state: UserTokenStatus,
	val imageUrl: String? = null,
) {
	companion object {
		val EMPTY = UserProfile(
			nickname = "",
			state = UserTokenStatus.INACTIVE,
			imageUrl = null,
		)
	}
}
