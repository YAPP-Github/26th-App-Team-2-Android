package com.yapp.breake.domain.usecase

import com.yapp.breake.core.model.user.UserStatus
import com.yapp.breake.domain.repository.RemoteLoginRepository
import com.yapp.breake.domain.repository.LocalTokenRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class LoginUseCaseImpl @Inject constructor(
	@Named("RemoteLoginRepo") private val remoteLoginRepository: RemoteLoginRepository,
	@Named("FakeLoginRepo") private val fakeRemoteLoginRepository: RemoteLoginRepository,
	private val localTokenRepository: LocalTokenRepository,
) : LoginUseCase {

	override operator fun invoke(
		authCode: String,
		provider: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserStatus> = fakeRemoteLoginRepository.flowLogin(
		provider = provider,
		authorizationCode = authCode,
		onError = onError,
	).onEach {
		// authCode 습득 성공 시 토큰과 유저 상태 (회원, 비회원) 저장
		localTokenRepository.updateUserToken(
			userAccessToken = it.accessToken,
			userRefreshToken = it.refreshToken,
			userStatus = it.status,
			onError = onError,
		)
	}.map {
		it.status
	}.onEach {
		// 만약 유저 토큰 상태가 HALF_SIGNUP이라면 authCode를 로컬에 저장
		if (it == UserStatus.HALF_SIGNUP) {
			localTokenRepository.updateAuthCode(
				authCode = authCode,
				onError = onError,
			)
			// 5분 후에 authCode 자동 삭제
			@OptIn(DelicateCoroutinesApi::class)
			GlobalScope.launch(kotlinx.coroutines.Dispatchers.IO) {
				delay(5 * 60 * 1000L)
				localTokenRepository.updateAuthCode(
					authCode = null,
					onError = onError,
				)
			}
		}
	}
}
