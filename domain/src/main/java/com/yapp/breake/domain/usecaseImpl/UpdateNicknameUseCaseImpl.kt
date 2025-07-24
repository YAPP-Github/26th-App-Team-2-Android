package com.yapp.breake.domain.usecaseImpl

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.repository.NicknameRepository
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : UpdateNicknameUseCase {

	override suspend fun invoke(nickname: String, onError: suspend (Throwable) -> Unit) {
		// AccessToken을 사용하여 닉네임 업데이트, 로컬에 닉네임 저장
		nicknameRepository.updateUserName(
			nickname = nickname,
			onError = onError,
		).collect {
			when (it.state) {
				// 닉네임 업데이트 성공 시
				UserStatus.ACTIVE -> {
					// DataStore에 저장된 authCode 삭제
					tokenRepository.clearLocalAuthCode(onError = onError)
				}

				// 닉네임 업데이트 실패 시
				else -> {
					// 에러 처리
					onError(Throwable("닉네임 업데이트에 실패했습니다"))
				}
			}
		}
	}
}
