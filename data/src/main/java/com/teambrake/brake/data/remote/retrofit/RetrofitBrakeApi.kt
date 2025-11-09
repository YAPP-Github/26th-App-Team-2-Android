package com.teambrake.brake.data.remote.retrofit

import com.skydoves.sandwich.ApiResponse
import com.teambrake.brake.data.remote.model.AppGroupResponse
import com.teambrake.brake.data.remote.model.BaseResponse
import com.teambrake.brake.data.remote.model.AppGroupRequest
import com.teambrake.brake.data.remote.model.GroupListResponse
import com.teambrake.brake.data.remote.model.LoginRequest
import com.teambrake.brake.data.remote.model.LoginResponse
import com.teambrake.brake.data.remote.model.MemberRequest
import com.teambrake.brake.data.remote.model.MemberResponse
import com.teambrake.brake.data.remote.model.RefreshRequest
import com.teambrake.brake.data.remote.model.RefreshResponse
import com.teambrake.brake.data.remote.model.SessionRequest
import com.teambrake.brake.data.remote.model.SessionResponse
import com.teambrake.brake.data.remote.model.StatisticResponse
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

	@GET("/v1/groups")
	suspend fun getAppGroups(): ApiResponse<GroupListResponse>

	@POST("/v1/groups")
	suspend fun createAppGroup(@Body request: AppGroupRequest): ApiResponse<AppGroupResponse>

	@PUT("/v1/groups/{groupId}")
	suspend fun updateAppGroup(
		@Path("groupId") groupId: Long,
		@Body request: AppGroupRequest,
	): ApiResponse<AppGroupResponse>

	@DELETE("/v1/groups/{groupId}")
	suspend fun deleteAppGroup(@Path("groupId") groupId: Long): ApiResponse<BaseResponse>

	@POST("/v1/session")
	suspend fun sendSession(@Body request: SessionRequest): ApiResponse<SessionResponse>

	@GET("/v1/statistics")
	suspend fun getStatistics(
		@Query("start") start: String,
		@Query("end") end: String,
	): ApiResponse<StatisticResponse>
}
