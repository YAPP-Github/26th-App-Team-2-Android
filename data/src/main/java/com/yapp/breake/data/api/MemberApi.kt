package com.yapp.breake.data.api

import com.yapp.breake.data.api.model.MemberRequest
import com.yapp.breake.data.api.model.MemberResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface MemberApi {
	@POST("/v1/members/me")
	suspend fun updateMemberInfo(
		@Body request: MemberRequest,
	): MemberResponse

	@GET("/v1/members/me")
	suspend fun getMemberInfo(): MemberResponse
}
