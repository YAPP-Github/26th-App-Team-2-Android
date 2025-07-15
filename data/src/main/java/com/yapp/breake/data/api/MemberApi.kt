package com.yapp.breake.data.api

import com.skydoves.sandwich.ApiResponse
import com.yapp.breake.data.api.model.MemberRequest
import com.yapp.breake.data.api.model.MemberResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface MemberApi {
	@PATCH("/v1/members/me")
	suspend fun updateMemberInfo(
		@Header("Authorization") authCode: String,
		@Body request: MemberRequest,
	): ApiResponse<MemberResponse>

	@GET("/v1/members/me")
	suspend fun getMemberInfo(): ApiResponse<MemberResponse>
}
