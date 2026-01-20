package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.model.result.success.OfflineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.OnlineAuthorizedSuccess
import com.teambrake.brake.domain.model.result.success.PreAuthSuccess
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
		onError: suspend (Throwable) -> Unit,
		onSuccess: suspend () -> Unit,
	) {
		// AccessToken을 사용하여 닉네임 업데이트, 로컬에 닉네임 저장
		val result = nicknameRepository.updateUserName(
			nickname = nickname,
			onError = onError,
		)
		when (result) {
			is BrakeResult.Success -> {
				val success = result.data
				val state = when (success) {
					is OnlineAuthorizedSuccess -> success.data.state
					is OfflineAuthorizedSuccess -> success.data.state
					is PreAuthSuccess -> success.data.state
				}
				when (state) {
					// 닉네임 업데이트 성공 시, 오프라인 모드 사용 시
					UserStatus.ACTIVE, UserStatus.OFFLINE -> {
						// DataStore에 저장된 authCode 삭제
						tokenRepository.clearLocalAuthCode(onError = onError)
						// 닉네임 업데이트 성공 후 콜백 호출
						onSuccess()
					}

					// 닉네임 업데이트 실패 시
					else -> {
						// 에러 처리
						onError(Throwable("닉네임 업데이트에 실패했습니다"))
					}
				}
			}
			is BrakeResult.Error -> {
				val error = result.error
				when (error) {
					is UndefinedExceptionError -> {
						onError(error.exception)
					}
					else -> {
						onError(Throwable("닉네임 업데이트 중 오류가 발생했습니다"))
					}
				}
			}
		}
	}
}
