package com.yapp.breake.data.repository

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserProfile
import com.yapp.breake.data.api.MemberApi
import com.yapp.breake.data.api.model.MemberRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.data.util.safeNetworkCall
import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
	private val memberApi: MemberApi,
) : UserProfileRepository {

	override fun getUserProfile(): Flow<ResponseResult<UserProfile>> = safeNetworkCall(
		apiCall = { memberApi.getMemberInfo() },
		mapper = { it.toData() },
	)

	override fun updateUserProfile(nickname: String): Flow<ResponseResult<UserProfile>> =
		safeNetworkCall(
			apiCall = { memberApi.updateMemberInfo(MemberRequest(nickname)) },
			mapper = { it.toData() },
		)
}
