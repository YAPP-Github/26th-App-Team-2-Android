package com.yapp.breake.core.auth.google

import android.content.Context
import androidx.activity.result.IntentSenderRequest
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor() {
	private lateinit var authorizationRequest: AuthorizationRequest
	private lateinit var credentialManager: CredentialManager

	fun initializeAuthorizationRequest(context: Context, serverClientId: String) {
		authorizationRequest = AuthorizationRequest.builder()
			.setRequestedScopes(
				listOf(
					Scope(GOOGLE_OAUTH2_OPEN_ID),
					Scope(GOOGLE_OAUTH2_EMAIL),
					Scope(GOOGLE_OAUTH2_PROFILE),
				),
			)
			.requestOfflineAccess(serverClientId)
			.build()
		credentialManager = CredentialManager.create(context)
	}

	fun requestGoogleAuthorization(
		context: Context,
		onRequestGoogleAuth: (IntentSenderRequest) -> Unit,
		onFailure: () -> Unit,
		coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob()),
	) {
		var attemptCount = 0
		val maxAttempts = 2
		val retryDelayMs = 10L

		fun attemptAuthorization() {
			attemptCount++

			Identity.getAuthorizationClient(context)
				.authorize(authorizationRequest)
				.addOnSuccessListener { authorizationResult ->
					authorizationResult.pendingIntent?.let { pendingIntent ->
						Timber.d("Google One Tap 로그인 창 실행")

						val intentSenderRequest = IntentSenderRequest.Builder(
							pendingIntent.intentSender,
						).build()
						onRequestGoogleAuth(intentSenderRequest)
					} ?: run {
						Timber.e("Google One Tap 로그인 창 실행 실패: pendingIntent is null")
						signOutGoogleAuth()

						if (attemptCount < maxAttempts) {
							Timber.d("Google Authorization 재시도 $attemptCount/$maxAttempts")
							coroutineScope.launch {
								delay(retryDelayMs)
								attemptAuthorization()
							}
						} else {
							onFailure()
						}
					}
				}
				.addOnFailureListener { exception ->
					Timber.e(exception, "Google 로그인 창 실행 실패")
					signOutGoogleAuth()

					if (attemptCount < maxAttempts) {
						Timber.d("Google Authorization 재시도 $attemptCount/$maxAttempts")
						coroutineScope.launch {
							delay(retryDelayMs)
							attemptAuthorization()
						}
					} else {
						onFailure()
					}
				}
		}

		attemptAuthorization()
	}

	/**
	 * Google Credential 상태 초기화
	 *
	 * 로그아웃, 회원탈퇴 시 반드시 호출해야 함, 그렇지 않으면 재로그인 시 이전에 로그인했던 구글 계정으로만 선택됨
	 */
	fun signOutGoogleAuth() {
		CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
			credentialManager.clearCredentialState(ClearCredentialStateRequest())
			Timber.i("Google Credential 상태가 초기화되었습니다.")
		}
	}

	companion object {
		// https://developers.google.com/identity/protocols/oauth2/scopes#oauth2
		const val GOOGLE_OAUTH2_OPEN_ID = "openid"
		const val GOOGLE_OAUTH2_EMAIL = "https://www.googleapis.com/auth/userinfo.email"
		const val GOOGLE_OAUTH2_PROFILE = "https://www.googleapis.com/auth/userinfo.profile"
	}
}
