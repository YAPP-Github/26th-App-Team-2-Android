package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.usecase.GetNicknameUseCase
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
