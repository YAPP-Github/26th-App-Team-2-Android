package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LocalApiCallError
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
	): BrakeResult<Unit, UpdateNicknameUseCaseError> {
		// AccessToken을 사용하여 닉네임 업데이트, 로컬에 닉네임 저장
		val result = nicknameRepository.updateUserName(nickname = nickname)

		return when {
			// Success 케이스: 닉네임 업데이트 성공
			result.isSuccess -> {
				// DataStore에 저장된 authCode 삭제
				val clearResult = tokenRepository.clearLocalAuthCode()

				when {
					clearResult.isSuccess -> {
						BrakeResult.Success(Unit)
					}

					clearResult.isFailure -> {
						val exception = clearResult.exceptionOrNull()
						BrakeResult.Error(LocalApiCallError(exception ?: Throwable("AuthCode 삭제 실패")))
					}

					else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
				}
			}

			// Failure 케이스: 닉네임 업데이트 실패
			result.isFailure -> {
				val exception = result.exceptionOrNull()
				BrakeResult.Error(LocalApiCallError(exception ?: Throwable("닉네임 업데이트에 실패했습니다")))
			}

			else -> BrakeResult.Error(LocalApiCallError(Exception("예상치 못한 오류")))
		}
	}
}
