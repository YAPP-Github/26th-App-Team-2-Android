package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {

	suspend fun insertApp(parentGroupId: Long, app: App)

	fun observeApp(): Flow<List<App>>

	suspend fun getAppGroupIdByPackage(packageName: String): Long?

	suspend fun deleteAppByParentGroupId(parentGroupId: Long)

	suspend fun clearApps()
}
