package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserToken
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
	suspend operator fun invoke(
		authAccessToken: String,
		provider: String = "KAKAO",
	): Flow<UserToken>
}
