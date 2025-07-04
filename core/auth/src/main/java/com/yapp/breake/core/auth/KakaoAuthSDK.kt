package com.yapp.breake.core.auth

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.yapp.breake.core.auth.model.kakao.KakaoUser
import com.yapp.breake.core.auth.model.LoginAccessToken
import com.yapp.breake.core.auth.model.kakao.KakaoLoginAccessToken
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class KakaoAuthSDK @Inject constructor() : LoginAuthSDK {

	override suspend fun login(context: Context): Result<LoginAccessToken> = runCatching {
		suspendCancellableCoroutine { continuation ->
			Timber.Forest.d("카카오 로그인 시도")
			val userApiClient = UserApiClient.instance
			Timber.Forest.d("UserApiClient 인스턴스: $userApiClient")
			val callback: (OAuthToken?, Throwable?) -> Unit = callback@{ token, error ->
				if (!continuation.isActive) {
					Timber.Forest.w("Continuation이 활성화되지 않음, 콜백 스킵")
					return@callback
				}

				if (error != null) {
					// 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
					// 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
					if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
						continuation.resumeWithException(CancellationException(error.message))
						Timber.Forest.d("카카오 로그인 취소됨: ${error.message}")
						return@callback
					}

					Timber.Forest.e(error, "카카오 로그인 실패")
					continuation.resumeWithException(error)
				} else if (token != null) {
					continuation.resume(KakaoLoginAccessToken(token.accessToken))
				}
			}

			// 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
			if (userApiClient.isKakaoTalkLoginAvailable(context)) {
				Timber.Forest.d("카카오톡이 설치되어 있음, 카카오톡으로 로그인 시도")
				userApiClient.loginWithKakaoTalk(context) { token, error ->
					if (error != null) {
						if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
							continuation.resumeWithException(CancellationException(error.message))
							return@loginWithKakaoTalk
						}

						// 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
						Timber.Forest.e(error, "카카오톡 로그인 실패, 카카오계정으로 로그인 시도")
						userApiClient.loginWithKakaoAccount(context, callback = callback)
					} else if (token != null) {
						Timber.Forest.d("카카오톡 로그인 성공: ${token.accessToken}")
						continuation.resume(KakaoLoginAccessToken(token.accessToken))
					}
				}
			} else {
				Timber.Forest.d("카카오톡이 설치되어 있지 않음, 카카오계정으로 로그인 시도")
				userApiClient.loginWithKakaoAccount(context, callback = callback)
			}
		}
	}

	// 성공 여부에 관계없이 항상 토큰 폐기
	override suspend fun logout(): Result<Unit> = runCatching {
		suspendCancellableCoroutine { continuation ->
			UserApiClient.instance.logout { error ->
				if (error != null) {
					Timber.Forest.e(error, "카카오 로그아웃 실패")
					continuation.resumeWithException(error)
				} else {
					Timber.Forest.d("카카오 로그아웃 성공")
					continuation.resume(Unit)
				}
			}
		}
	}

	// 로그아웃과 다르게 성공시에만 토큰 폐기 및 로그아웃
	override suspend fun unlink(): Result<Unit> = runCatching {
		suspendCancellableCoroutine { continuation ->
			UserApiClient.instance.unlink { error ->
				if (error != null) {
					Timber.Forest.e(error, "카카오 계정 연결 해제 실패")
					continuation.resumeWithException(error)
				} else {
					Timber.Forest.d("카카오 계정 연결 해제 성공")
					continuation.resume(Unit)
				}
			}
		}
	}

	// 카카오 사용자 정보 조회
	override suspend fun getAuthUserInfo(): Result<KakaoUser> = runCatching {
		suspendCancellableCoroutine { continuation ->
			UserApiClient.instance.me { user, error ->
				if (error != null) {
					Timber.Forest.e(error, "카카오 사용자 정보 조회 실패")
					continuation.resumeWithException(error)
				} else if (user != null) {
					val kakaoUserInfo = KakaoUser(
						name = user.kakaoAccount?.name ?: "",
						email = user.kakaoAccount?.email ?: "",
					)
					continuation.resume(kakaoUserInfo)
				}
			}
		}
	}
}
