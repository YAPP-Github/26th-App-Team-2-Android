package com.teambrake.brake.core.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserName(
	val nickname: String,
	val state: UserStatus,
)
