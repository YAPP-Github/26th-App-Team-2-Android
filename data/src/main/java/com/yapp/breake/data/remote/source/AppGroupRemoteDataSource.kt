package com.yapp.breake.data.remote.source

import com.yapp.breake.core.model.app.AppGroup

internal interface AppGroupRemoteDataSource {

	suspend fun createAppGroup(
		appGroup: AppGroup,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun updateAppGroup(
		appGroup: AppGroup,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun deleteAppGroup(
		groupId: Long,
		onSuccess: suspend () -> Unit,
		onError: suspend (Throwable) -> Unit = {},
	)
}
