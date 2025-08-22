package com.yapp.breake.data.local.source

import com.yapp.breake.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppLocalDataSource {

	suspend fun insertApp(
		parentGroupId: Long,
		app: App,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun insertApps(
		parentGroupId: Long,
		apps: List<App>,
		onError: suspend (Throwable) -> Unit = {},
	)

	fun observeApp(
		onError: suspend (Throwable) -> Unit = {},
	): Flow<List<App>>

	suspend fun getAppGroupIdByPackage(
		packageName: String,
		onError: suspend (Throwable) -> Unit = {},
	): Long?

	suspend fun deleteAppByParentGroupId(
		parentGroupId: Long,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun clearApps(
		onError: suspend (Throwable) -> Unit = {},
	)
}
