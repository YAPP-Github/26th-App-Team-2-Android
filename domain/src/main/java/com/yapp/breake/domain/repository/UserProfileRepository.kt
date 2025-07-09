package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
	suspend fun getUserProfile(): Flow<UserProfile>

	suspend fun updateUserProfile(nickname: String): Flow<UserProfile>
}
