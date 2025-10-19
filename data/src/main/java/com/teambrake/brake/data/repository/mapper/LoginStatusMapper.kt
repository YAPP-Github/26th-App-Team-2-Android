package com.teambrake.brake.data.repository.mapper

import com.teambrake.brake.core.model.user.UserStatus

internal fun String.toLoginStatus(): UserStatus = when (this) {
	"ACTIVE" -> UserStatus.ACTIVE
	"HOLD" -> UserStatus.HALF_SIGNUP
	else -> UserStatus.INACTIVE
}
