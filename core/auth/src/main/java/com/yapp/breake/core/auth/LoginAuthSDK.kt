package com.yapp.breake.core.auth

import android.content.Context
import com.yapp.breake.core.auth.model.AuthUser
import com.yapp.breake.core.auth.model.LoginAccessToken

interface LoginAuthSDK {
	suspend fun login(context: Context): Result<LoginAccessToken>
	suspend fun logout(): Result<Unit>
	suspend fun unlink(): Result<Unit>
	suspend fun getAuthUserInfo(): Result<AuthUser>
}
