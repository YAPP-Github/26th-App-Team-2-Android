package com.teambrake.brake.data.remote.source

import com.teambrake.brake.core.model.app.AppGroup
import kotlinx.coroutines.flow.Flow

internal interface AppGroupRemoteDataSource {

	fun getAppGroups(
		onError: suspend (Throwable) -> Unit = {},
	): Flow<List<AppGroup>>

	fun createAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit = {},
	): Flow<AppGroup>

	fun updateAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit = {},
	): Flow<AppGroup>

	suspend fun deleteAppGroup(
		groupId: Long,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)
}
