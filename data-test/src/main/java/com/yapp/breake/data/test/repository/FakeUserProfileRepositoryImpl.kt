package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.response.ResponseResult
import com.yapp.breake.core.model.user.UserProfile
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeUserProfileRepositoryImpl @Inject constructor() : UserProfileRepository {

	override fun getUserProfile(): Flow<ResponseResult<UserProfile>> = flow {
		emit(
			ResponseResult.Success(
				UserProfile(
					nickname = "FakeUser",
					state = UserTokenStatus.ACTIVE,
					imageUrl = null,
				),
			),
		)
	}

	override fun updateUserProfile(nickname: String): Flow<ResponseResult<UserProfile>> = flow {
		emit(
			ResponseResult.Success(
				UserProfile(
					nickname = nickname,
					state = UserTokenStatus.ACTIVE,
					imageUrl = null,
				),
			),
		)
	}
}
