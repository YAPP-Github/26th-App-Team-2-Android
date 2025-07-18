package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import kotlinx.coroutines.flow.Flow

/**
 * 카카오 로그인 이후 AuthCode를 이용하여 로그인하는 UseCase
 *
 * 로그인 성공 시 UserStatus를 반환하며, 실패 시 onError 콜백을 호출
 */
interface LoginUseCase {
	operator fun invoke(
		authCode: String,
		provider: String = "KAKAO",
		onError: suspend (Throwable) -> Unit,
	): Flow<UserStatus>
}
