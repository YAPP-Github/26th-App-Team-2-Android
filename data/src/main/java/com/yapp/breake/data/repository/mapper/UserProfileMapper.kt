package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserProfile
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.data.api.model.MemberResponse

internal fun MemberResponse.toData(): UserProfile = UserProfile(
	nickname = this.data.nickname,
	state = when (this.data.state) {
		"ACTIVE" -> UserTokenStatus.ACTIVE
		"HALF_SIGNUP" -> UserTokenStatus.HALF_SIGNUP
		else -> UserTokenStatus.INACTIVE
	},
	imageUrl = null,
)
