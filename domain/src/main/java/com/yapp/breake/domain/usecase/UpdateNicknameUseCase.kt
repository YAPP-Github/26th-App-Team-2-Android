package com.yapp.breake.domain.usecase

interface UpdateNicknameUseCase {
	suspend operator fun invoke(nickname: String, onError: suspend (Throwable) -> Unit)
}
