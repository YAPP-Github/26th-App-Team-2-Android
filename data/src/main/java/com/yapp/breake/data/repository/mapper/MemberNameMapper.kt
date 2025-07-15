package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.data.api.model.MemberResponse

internal fun MemberResponse.toData(): UserName = UserName(
	nickname = this.data.nickname,
	state = when (this.data.state) {
		"ACTIVE" -> UserStatus.ACTIVE
		"HOLD" -> UserStatus.HALF_SIGNUP
		else -> UserStatus.INACTIVE
	},
)
