package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.response.ResponseResult
import kotlinx.coroutines.flow.Flow

interface UpdateNicknameUseCase {
	operator fun invoke(nickname: String): Flow<ResponseResult<Unit>>
}
