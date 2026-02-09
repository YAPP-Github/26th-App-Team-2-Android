package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.exception.toApiCallError
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.GetNicknameUseCaseError
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.usecase.GetNicknameUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

class GetNicknameUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
) : GetNicknameUseCase {
	override fun invoke(): Flow<BrakeResult<String, GetNicknameUseCaseError>> =
		nicknameRepository.getNickname()
			.map { BrakeResult.Success(it) as BrakeResult<String, GetNicknameUseCaseError> }
			.catch { e ->
				emit(BrakeResult.Error(e.toApiCallError()))
			}
}
