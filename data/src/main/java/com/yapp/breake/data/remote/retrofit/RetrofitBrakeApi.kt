package com.yapp.breake.data.remote.retrofit

import com.skydoves.sandwich.ApiResponse
import com.yapp.breake.data.remote.model.LoginRequest
import com.yapp.breake.data.remote.model.LoginResponse
import com.yapp.breake.data.remote.model.MemberRequest
import com.yapp.breake.data.remote.model.MemberResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST

internal interface RetrofitBrakeApi {
	@POST("/v1/auth/login")
	suspend fun getTokens(
		@Body request: LoginRequest,
	): ApiResponse<LoginResponse>

	@PATCH("/v1/members/me")
	suspend fun updateMemberName(
		@Header("Authorization") authCode: String,
		@Body request: MemberRequest,
	): ApiResponse<MemberResponse>

	@GET("/v1/members/me")
	suspend fun getMemberName(): ApiResponse<MemberResponse>
}
