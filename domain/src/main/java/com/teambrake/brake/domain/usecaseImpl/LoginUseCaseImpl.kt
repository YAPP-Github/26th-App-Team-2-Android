package com.teambrake.brake.domain.usecaseImpl

import com.teambrake.brake.core.model.user.UserStatus
import com.teambrake.brake.domain.model.result.BrakeResult
import com.teambrake.brake.domain.model.result.error.LoginUseCaseError
import com.teambrake.brake.domain.model.result.error.UndefinedExceptionError
import com.teambrake.brake.domain.repository.NicknameRepository
import com.teambrake.brake.domain.repository.AuthRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.usecase.LoginUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Named

/**
 * 카카오 로그인 이후 AuthCode를 이용하여 로그인하는 UseCase
 *
 * 로그인 성공 시 UserStatus를 반환하며, 실패 시 Error 반환
 */
class LoginUseCaseImpl @Inject constructor(
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	private val authRepository: AuthRepository,
) : LoginUseCase {

	@OptIn(ExperimentalCoroutinesApi::class)
	override operator fun invoke(
		authCode: String,
		provider: String,
	): Flow<BrakeResult<UserStatus, LoginUseCaseError>> = tokenRepository.getRemoteTokens(
		provider = provider,
		authorizationCode = authCode,
	).map { userToken ->
		when (userToken.status) {
			UserStatus.ACTIVE -> {
				handleActiveUserLogin()
			}

			UserStatus.HALF_SIGNUP -> {
				handleHalfSignupUser()
			}

			else -> {
				// INACTIVE, OFFLINE 등 기타 상태
			}
		}
		BrakeResult.Success(userToken.status) as BrakeResult<UserStatus, LoginUseCaseError>
	}.catch { e ->
		emit(BrakeResult.Error(UndefinedExceptionError(e)))
	}

	private suspend fun handleActiveUserLogin() {
		// 닉네임 가져오기
		val nicknameResult = runCatching {
			nicknameRepository.getNickname()
				.catch { e -> throw e }
				.first()
		}

		when {
			nicknameResult.isSuccess -> {
				val nickname = nicknameResult.getOrNull() ?: return
				// 닉네임 저장
				val saveResult = nicknameRepository.saveLocalUserName(nickname = nickname)

				if (saveResult.isFailure) {
					// 닉네임 저장 실패 시 로컬 이름을 지움
					nicknameRepository.clearLocalName()
				}
			}

			nicknameResult.isFailure -> {
				// 닉네임 가져오기 실패 시 로컬 이름을 지움
				nicknameRepository.clearLocalName()
			}
		}
	}

	private suspend fun handleHalfSignupUser() {
		val updateResult = authRepository.updateOnboardingFlag(isComplete = false)

		when {
			updateResult.isSuccess -> {
				// 성공적으로 온보딩 플래그 업데이트됨
			}

			updateResult.isFailure -> {
				// 온보딩 플래그 업데이트 실패 시 로컬 이름을 지움
				nicknameRepository.clearLocalName()
			}
		}
	}
}
