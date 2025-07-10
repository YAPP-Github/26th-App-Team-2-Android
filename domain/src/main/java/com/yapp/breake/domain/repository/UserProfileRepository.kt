package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
	fun getUserProfile(): Flow<UserProfile>

	fun updateUserProfile(nickname: String): Flow<UserProfile>
}
