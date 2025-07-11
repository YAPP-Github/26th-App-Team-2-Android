package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserTokenStatus
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
	operator fun invoke(
		authAccessToken: String,
		provider: String = "KAKAO",
	): Flow<ResponseResult<UserTokenStatus>>
}
