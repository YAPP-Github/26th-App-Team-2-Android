package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.user.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
	fun getUserProfile(onError: suspend (Throwable) -> Unit): Flow<UserProfile>

	fun updateUserProfile(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<UserProfile>
}
