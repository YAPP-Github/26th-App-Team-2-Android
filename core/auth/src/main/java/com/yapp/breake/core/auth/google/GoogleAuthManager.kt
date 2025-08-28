package com.yapp.breake.core.auth.google

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.CancellationSignal
import androidx.activity.result.IntentSenderRequest
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialManagerCallback
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.playservices.CredentialProviderPlayServicesImpl
import androidx.credentials.playservices.CredentialProviderPlayServicesImpl.Companion.MIN_GMS_APK_VERSION
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.Scope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

@SuppressLint("RestrictedApi")
@Singleton
class GoogleAuthManager @Inject constructor(
	@ApplicationContext private val appContext: Context,
) {
	val credentialProvider = CredentialProviderPlayServicesImpl(appContext)
	private lateinit var authorizationRequest: AuthorizationRequest
	private lateinit var credentialManager: CredentialManager

	fun initializeAuthorizationRequest(context: Context, serverClientId: String) {
		authorizationRequest = AuthorizationRequest.builder()
			.setRequestedScopes(
				listOf(
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
		onAlertUpdateGooglePlayServices: () -> Unit,
	) {
		if (!credentialProvider.isAvailableOnDevice()) {
			val currentGmsApkVersion = GoogleApiAvailability().getApkVersion(appContext)
			Timber.e("Google Play Services 버전: $currentGmsApkVersion, 최소 필요 버전: $MIN_GMS_APK_VERSION")
			onAlertUpdateGooglePlayServices()
			return
		}

		var attemptCount = 0
		val maxAttempts = 2
		val retryDelayMs = 200L

		fun attemptAuthorization() {
			attemptCount++
			Timber.d("Google Authorization 재시도 $attemptCount/$maxAttempts")

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

						if (attemptCount < maxAttempts) {
							CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
								signOutGoogleAuth()
								delay(retryDelayMs)
								attemptAuthorization()
							}
						} else {
							onFailure()
						}
					}
				}
				.addOnFailureListener { exception ->
					onFailure()
				}
		}

		attemptAuthorization()
	}

	/**
	 * Google Credential 상태 초기화
	 *
	 * 로그아웃, 회원탈퇴 시 반드시 호출해야 함, 그렇지 않으면 재로그인 시 이전에 로그인했던 구글 계정으로만 선택됨
	 */

	@SuppressLint("RestrictedApi")
	fun signOutGoogleAuth() {
		Timber.i("Google Service Version : ${GoogleApiAvailability().getApkVersion(appContext)}")
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
			CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
				// API 34 이상에서 안전하게 사용 가능
				// API 34 미만에서는 credentialProvider.isAvailableOnDevice() == false 인 경우 에러 발생
				credentialManager.clearCredentialState(ClearCredentialStateRequest())
				Timber.i("API 34+ : Google Credential 상태가 초기화되었습니다.")
			}
		} else {
			// API 33 이하에서 credentialProvider.isAvailableOnDevice() == true 인 경우
			// credentialManager.clearCredentialState(ClearCredentialStateRequest()) 실행 가능
			CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
				suspendCancellableCoroutine { continuation ->
					val canceller = CancellationSignal()
					val callback =
						object : CredentialManagerCallback<Void?, ClearCredentialException> {
							override fun onResult(result: Void?) {
								if (continuation.isActive) {
									Timber.i("API 33- : Google Credential 상태가 초기화되었습니다.")
									continuation.resume(Unit) { cause, _, _ -> }
								}
							}

							override fun onError(e: ClearCredentialException) {
								if (continuation.isActive) {
									Timber.e(e, "Google Credential 상태 초기화에 실패했습니다.")
									continuation.resumeWithException(e)
								}
							}
						}
					credentialProvider.onClearCredential(
						ClearCredentialStateRequest(),
						canceller,
						Runnable::run,
						callback,
					)
				}
			}
		}
	}

	companion object {
		// https://developers.google.com/identity/protocols/oauth2/scopes#oauth2
		const val GOOGLE_OAUTH2_OPEN_ID = "openid"
		const val GOOGLE_OAUTH2_EMAIL = "https://www.googleapis.com/auth/userinfo.email"
		const val GOOGLE_OAUTH2_PROFILE = "https://www.googleapis.com/auth/userinfo.profile"
	}
}
