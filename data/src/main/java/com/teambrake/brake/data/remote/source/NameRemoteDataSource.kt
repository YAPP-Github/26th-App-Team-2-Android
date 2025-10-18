package com.teambrake.brake.data.remote.source

import com.teambrake.brake.data.remote.model.MemberResponse
import kotlinx.coroutines.flow.Flow

interface NameRemoteDataSource {
	fun updateUserName(
		nickname: String,
		onError: suspend (Throwable) -> Unit,
	): Flow<MemberResponse>

	fun getUserName(onError: suspend (Throwable) -> Unit): Flow<MemberResponse>
}
