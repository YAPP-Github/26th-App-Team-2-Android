package com.yapp.breake.core.auth

import android.content.Context
import com.yapp.breake.core.model.auth.AuthUser
import com.yapp.breake.core.model.auth.LoginAccessToken

interface LoginAuthSDK {
	suspend fun login(context: Context): Result<LoginAccessToken>
	suspend fun logout(): Result<Unit>
	suspend fun unlink(): Result<Unit>
	suspend fun getAuthUserInfo(): Result<AuthUser>
}
