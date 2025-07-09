package com.yapp.breake.data.test.repository

import com.yapp.breake.core.model.user.UserProfile
import com.yapp.breake.core.model.user.UserTokenStatus
import com.yapp.breake.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class FakeUserProfileRepositoryImpl @Inject constructor() : UserProfileRepository {

	override suspend fun getUserProfile(): Flow<UserProfile> = flow {
		emit(
			UserProfile(
				nickname = "FakeUser",
				state = UserTokenStatus.ACTIVE,
				imageUrl = null,
			),
		)
	}

	override suspend fun updateUserProfile(nickname: String): Flow<UserProfile> = flow {
		emit(
			UserProfile(
				nickname = nickname,
				state = UserTokenStatus.ACTIVE,
				imageUrl = null,
			),
		)
	}
}
