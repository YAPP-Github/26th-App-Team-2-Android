package com.yapp.breake.data.repository

import com.yapp.breake.core.model.user.UserProfile
import com.yapp.breake.data.api.MemberApi
import com.yapp.breake.data.api.model.MemberRequest
import com.yapp.breake.data.repository.mapper.toData
import com.yapp.breake.data.util.safeNetworkCall
import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
	private val memberApi: MemberApi,
) : UserProfileRepository {

	override fun getUserProfile(): Flow<UserProfile> = flow {
		safeNetworkCall {
			memberApi.getMemberInfo().toData()
		}.let { emit(it) }
	}

	override fun updateUserProfile(nickname: String): Flow<UserProfile> = flow {
		safeNetworkCall {
			memberApi.updateMemberInfo(MemberRequest(nickname)).toData()
		}.let { emit(it) }
	}
}
