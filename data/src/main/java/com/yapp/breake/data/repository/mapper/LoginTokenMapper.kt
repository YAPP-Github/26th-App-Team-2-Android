package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.data.api.model.LoginResponse

internal fun LoginResponse.toData(): UserToken = UserToken(
	accessToken = this.data.accessToken,
	refreshToken = this.data.refreshToken,
	status = when (this.data.memberState) {
		"ACTIVE" -> UserStatus.ACTIVE
		"HOLD" -> UserStatus.HALF_SIGNUP
		else -> UserStatus.INACTIVE
	},
)
