package com.yapp.breake.core.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import timber.log.Timber
import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor() {
	private lateinit var credentialManager: CredentialManager
	private lateinit var googleIdOption: GetSignInWithGoogleOption

	fun initializeGoogleAuthManager(applicationContext: Context, webClientId: String) {
		credentialManager = CredentialManager.create(applicationContext)
		val newNonce: String = BigInteger(130, SecureRandom()).toString(32)

		googleIdOption = GetSignInWithGoogleOption.Builder(
			serverClientId = webClientId,
		).setNonce(newNonce).build()
	}

	suspend fun signInWithGoogle(context: Context): Result<GoogleIdTokenCredential> {
		val request = GetCredentialRequest.Builder()
			.addCredentialOption(googleIdOption)
			.build()

		return try {
			// OAuth 요청이 발생 지점
			val result = credentialManager.getCredential(
				request = request,
				// Activity Context 필요
				context = context,
			)
			handleSignInResult(result)
		} catch (e: GetCredentialCancellationException) {
			// 사용자가 직접 취소한 경우
			Timber.i("사용자가 Google 로그인을 취소했습니다")
			Result.failure(e)
		} catch (e: GetCredentialException) {
			Timber.e(e, "Google Credential 요청 실패")
			Result.failure(e)
		}
	}

	private fun handleSignInResult(result: GetCredentialResponse): Result<GoogleIdTokenCredential> {
		val credential = result.credential

		return when (credential) {
			is CustomCredential -> {
				if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
					try {
						val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
						Result.success(googleIdTokenCredential)
					} catch (e: GoogleIdTokenParsingException) {
						Timber.e(e, "Google Credential ID Token 파싱 실패")
						Result.failure(e)
					}
				} else {
					Timber.e("예상치 못한 credential 타입: ${credential.type}")
					Result.failure(IllegalStateException("Unexpected credential type"))
				}
			}

			else -> Result.failure(IllegalStateException("Not Allowed credential type"))
		}
	}

	suspend fun clearGoogleCredentialState() {
		credentialManager.clearCredentialState(ClearCredentialStateRequest())
		Timber.i("Google Credential 상태가 초기화되었습니다.")
	}
}
