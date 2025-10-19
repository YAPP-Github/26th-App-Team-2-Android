package com.teambrake.brake.data.repository.mapper

import com.teambrake.brake.core.model.user.UserName
import com.teambrake.brake.data.remote.model.MemberResponse

internal fun MemberResponse.toData(): UserName = UserName(
	nickname = this.data.nickname,
	state = this.data.state.toLoginStatus(),
)
