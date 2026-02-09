package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.exception.toApiCallError
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.UpdateNicknameUseCaseError
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.usecase.UpdateNicknameUseCase
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : UpdateNicknameUseCase {

	override suspend fun invoke(
		nickname: String,
	): BrakeResult<Unit, UpdateNicknameUseCaseError> =
		// AccessToken을 사용하여 닉네임 업데이트, 로컬에 닉네임 저장
		nicknameRepository.updateUserName(nickname = nickname).fold(
			onSuccess = {
				tokenRepository.clearLocalAuthCode().fold(
					onSuccess = {
						BrakeResult.Success(Unit)
					},
					onFailure = { e ->
						BrakeResult.Error(e.toApiCallError())
					},
				)
			},
			onFailure = { e ->
				BrakeResult.Error(e.toApiCallError())
			},
		)
}
