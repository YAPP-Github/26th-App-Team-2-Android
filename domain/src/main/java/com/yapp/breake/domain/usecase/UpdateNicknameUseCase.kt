package com.yapp.breake.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UpdateNicknameUseCase {
	suspend operator fun invoke(
		nickname: String,
	): Flow<Unit>
}
