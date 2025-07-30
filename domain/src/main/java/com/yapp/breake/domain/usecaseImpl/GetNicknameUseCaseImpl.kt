package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.domain.repository.NicknameRepository
import com.yapp.breake.domain.usecase.GetNicknameUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetNicknameUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
) : GetNicknameUseCase {
	override fun invoke(
		onError: suspend (Throwable) -> Unit,
	): Flow<String> = nicknameRepository.getLocalUserName(onError = onError)
}
