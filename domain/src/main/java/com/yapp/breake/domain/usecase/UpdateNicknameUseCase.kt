package com.yapp.breake.domain.usecase

/**
 * 회원 가입 시 또는 닉네임 변경 시 사용되는 UseCase
 */
interface UpdateNicknameUseCase {
	suspend operator fun invoke(nickname: String, onError: suspend (Throwable) -> Unit)
}
