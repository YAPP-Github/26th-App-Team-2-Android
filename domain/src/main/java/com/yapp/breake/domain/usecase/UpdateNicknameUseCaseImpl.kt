package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.LocalInfoRepository
import com.yapp.breake.domain.repository.RemoteNameRepository
import com.yapp.breake.domain.repository.LocalTokenRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject
import javax.inject.Named

class UpdateNicknameUseCaseImpl @Inject constructor(
	@Named("RemoteNameRepo") private val remoteNameRepository: RemoteNameRepository,
	private val localTokenRepository: LocalTokenRepository,
	private val localInfoRepository: LocalInfoRepository,
) : UpdateNicknameUseCase {

	override suspend fun invoke(nickname: String, onError: suspend (Throwable) -> Unit) {

		// authCode를 DataStore 에서 가져옴
		val authCode = localTokenRepository.getAuthCode(onError = onError)
			.firstOrNull() ?: run {
			// authCode가 DataStore에 없으면 에러 처리
			onError(Throwable("세션이 만료되었습니다"))
			return
		}

		// 닉네임 업데이트 시도
		remoteNameRepository.updateUserName(
			authCode = authCode,
			nickname = nickname,
			onError = onError,
		).collect {
			when (it.state) {
				// 닉네임 업데이트 성공 시
				UserStatus.ACTIVE -> {
					// DataStore에 저장된 authCode 삭제
					localTokenRepository.updateAuthCode(
						authCode = null,
						onError = onError,
					)
					// 닉네임 DataStore에 저장
					localInfoRepository.updateNickname(
						nickname = it.nickname,
						onError = onError,
					)
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
