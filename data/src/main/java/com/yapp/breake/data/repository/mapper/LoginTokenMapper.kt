package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.data.api.model.LoginResponse

internal fun LoginResponse.toData(): UserToken = UserToken(
	accessToken = accessToken,
	refreshToken = refreshToken,
	status = when (memberState) {
		"ACTIVE" -> UserTokenStatus.ACTIVE
		else -> UserTokenStatus.HALF_SIGNUP
	},
)
