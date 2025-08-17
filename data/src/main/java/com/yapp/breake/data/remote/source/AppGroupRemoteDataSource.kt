package com.yapp.breake.data.remote.source

import com.yapp.breake.core.model.app.AppGroup
import kotlinx.coroutines.flow.Flow

internal interface AppGroupRemoteDataSource {

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
