package com.yapp.breake.core.auth.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.yapp.breake.core.auth.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthManager @Inject constructor(
	@ApplicationContext private val applicationContext: Context,
) {
	private val credentialManager = CredentialManager.create(applicationContext)

	suspend fun signInWithGoogle(
		context: Context,
		nonce: String?,
	): Result<GoogleIdTokenCredential> {
		val googleIdOption = GetGoogleIdOption.Builder()
			// 이미 인증되지 않은 계정 포함
			.setFilterByAuthorizedAccounts(false)
			.setServerClientId(BuildConfig.GOOGLE_AUTH_WEB_CLIENT_ID)
			// 자동 계정 선택 비활성화
			.setAutoSelectEnabled(false)
			.apply { nonce?.let { setNonce(it) } }
			.build()

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
		} catch (e: GetCredentialException) {
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
						Result.failure(e)
					}
				} else {
					Result.failure(IllegalStateException("Unexpected credential type"))
				}
			}
			else -> Result.failure(IllegalStateException("Not Allowed credential type"))
		}
	}
}
