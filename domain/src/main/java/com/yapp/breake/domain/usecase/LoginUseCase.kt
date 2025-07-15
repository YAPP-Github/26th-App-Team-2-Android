package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {
	operator fun invoke(
		authCode: String,
		provider: String = "KAKAO",
		onError: suspend (Throwable) -> Unit,
	): Flow<UserStatus>
}
