package com.yapp.breake.data.remote.source

import com.yapp.breake.data.remote.model.AppGroupRequest
import com.yapp.breake.data.remote.model.AppGroupResponse
import kotlinx.coroutines.flow.Flow

internal interface AppGroupRemoteDataSource {

	fun createAppGroup(
		request: AppGroupRequest,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroupResponse>

	fun updateAppGroup(
		groupId: Long,
		request: AppGroupRequest,
		onError: suspend (Throwable) -> Unit,
	): Flow<AppGroupResponse>

	fun deleteAppGroup(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	): Flow<Unit>
}
