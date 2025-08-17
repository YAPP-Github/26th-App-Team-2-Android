package com.yapp.breake.data.remote.retrofit

import com.skydoves.sandwich.ApiResponse
import com.yapp.breake.data.remote.model.AppGroupResponse
import com.yapp.breake.data.remote.model.BaseResponse
import com.yapp.breake.data.remote.model.AppGroupRequest
import com.yapp.breake.data.remote.model.LoginRequest
import com.yapp.breake.data.remote.model.LoginResponse
import com.yapp.breake.data.remote.model.MemberRequest
import com.yapp.breake.data.remote.model.MemberResponse
import com.yapp.breake.data.remote.model.RefreshRequest
import com.yapp.breake.data.remote.model.RefreshResponse
import com.yapp.breake.data.remote.model.SessionRequest
import com.yapp.breake.data.remote.model.SessionResponse
import com.yapp.breake.data.remote.model.StatisticResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RetrofitBrakeApi {

	@POST("/v1/auth/login")
	suspend fun getTokens(
		@Body request: LoginRequest,
	): ApiResponse<LoginResponse>

	@PATCH("/v1/members/me")
	suspend fun updateMemberName(
		@Body request: MemberRequest,
	): ApiResponse<MemberResponse>

	@GET("/v1/members/me")
	suspend fun getMemberName(): ApiResponse<MemberResponse>

	@POST("/v1/auth/refresh")
	suspend fun refreshTokens(
		@Body refreshRequest: RefreshRequest,
	): ApiResponse<RefreshResponse>

	@DELETE("/v1/members/me")
	suspend fun deleteMemberName(): ApiResponse<BaseResponse>

	@POST("/v1/auth/logout")
	suspend fun logoutAuth(
		@Body accessToken: String,
	): ApiResponse<BaseResponse>

	@POST("/v1/groups")
	suspend fun createAppGroup(@Body request: AppGroupRequest): ApiResponse<AppGroupResponse>

	@PUT("/v1/groups/{groupId}")
	suspend fun updateAppGroup(
		@Path("groupId") groupId: Long,
		@Body request: AppGroupRequest,
	): ApiResponse<AppGroupResponse>

	@DELETE("/v1/groups/{groupId}")
	suspend fun deleteAppGroup(@Path("groupId") groupId: Long): ApiResponse<BaseResponse>

	@POST("/v1/sessions")
	suspend fun sendSession(@Body request: SessionRequest): ApiResponse<SessionResponse>

	@POST("/v1/sessions")
	suspend fun getStatistics(
		@Query("start") start: String,
		@Query("end") end: String,
	): ApiResponse<StatisticResponse>
}
