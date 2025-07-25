package com.yapp.breake.data.repository.mapper

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.core.model.user.UserToken
import com.yapp.breake.data.remote.model.RefreshResponse

internal fun RefreshResponse.toData(): UserToken = UserToken(
	accessToken = this.data.accessToken,
	refreshToken = this.data.refreshToken,
	// 자동 로그인은 유저 이름까지 등록된 상태로 가정
	status = UserStatus.ACTIVE,
)
