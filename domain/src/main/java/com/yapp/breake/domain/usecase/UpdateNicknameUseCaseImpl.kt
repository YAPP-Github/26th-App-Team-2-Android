package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.repository.NicknameRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("NicknameRepo") private val nicknameRepository: NicknameRepository,
	@Named("TokenRepo") private val tokenRepository: TokenRepository,
) : UpdateNicknameUseCase {

	override suspend fun invoke(nickname: String, onError: suspend (Throwable) -> Unit) {

		// DataStore에서 AccessToken 가져오기
		val accessToken = nicknameRepository.getLocalAccessToken(onError = onError)
			.firstOrNull() ?: run {
			// AccessToken DataStore에 없으면 에러 처리
			onError(Throwable("세션이 만료되었습니다"))
			return
		}

		// AccessToken을 사용하여 닉네임 업데이트, 로컬에 닉네임 저장
		nicknameRepository.updateUserName(
			accessToken = accessToken,
			nickname = nickname,
			onError = onError,
		).collect {
			when (it.state) {
				// 닉네임 업데이트 성공 시
				UserStatus.ACTIVE -> {
					// DataStore에 저장된 authCode 삭제
					tokenRepository.clearAuthCode(onError = onError)
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
