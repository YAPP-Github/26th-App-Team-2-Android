package com.yapp.breake.data.api

import com.yapp.breake.data.api.model.LoginRequest
import com.yapp.breake.data.api.model.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface LoginApi {
	@POST("/v1/auth/login")
	suspend fun getTokens(
		@Body request: LoginRequest,
	): LoginResponse
}
