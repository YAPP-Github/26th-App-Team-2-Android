package com.teambrake.brake.data.repository.mapper

import com.teambrake.brake.core.model.user.UserToken
import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.data.remote.model.LoginResponse

internal fun LoginResponse.toData(): UserToken = UserToken(
	accessToken = this.data.accessToken,
	refreshToken = this.data.refreshToken,
	status = when (this.data.memberState) {
		"ACTIVE" -> UserStatus.ACTIVE
		"HOLD" -> UserStatus.HALF_SIGNUP
		else -> UserStatus.INACTIVE
	},
)
