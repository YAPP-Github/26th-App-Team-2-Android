package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserName
import com.yapp.breake.data.remote.model.MemberResponse

internal fun MemberResponse.toData(): UserName = UserName(
	nickname = this.data.nickname,
	state = this.data.state.toLoginStatus(),
)
