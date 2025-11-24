package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.success.OfflineModeSuccess
import com.teambrake.brake.domain.model.result.success.OnlineModeSuccess
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * 카카오 로그인 이후 AuthCode를 이용하여 로그인하는 UseCase
 *
 * 로그인 성공 시 UserStatus를 반환하며, 실패 시 onError 콜백을 호출
 */
class LoginUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val sessionRepository: SessionRepository,
) : LoginUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override operator fun invoke(
		authCode: String,
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserStatus> = tokenRepository.getRemoteTokens(
		provider = provider,
		authorizationCode = authCode,
		onError = onError,
	)
		.map { userToken ->
			if (userToken.status == UserStatus.ACTIVE) {
				val result = nicknameRepository.getRemoteUserName()
				when (result) {
					is BrakeResult.Success -> {
						val success = result.data
						val nickname = when (success) {
							is OnlineModeSuccess -> success.data.nickname
							is OfflineModeSuccess -> success.data.nickname
						}
						nicknameRepository.saveLocalUserName(
							nickname = nickname,
							onError = onError,
						)
					}
					is BrakeResult.Error -> {
						// 원격 사용자 이름을 가져오는 데 실패한 경우 로컬 이름을 지웁니다.
						nicknameRepository.clearLocalName(onError = onError)
					}
				}
			} else {
				sessionRepository.updateLocalOnboardingFlag(
					isComplete = false,
					onError = onError,
				)
			}
			userToken.status
		}
}
