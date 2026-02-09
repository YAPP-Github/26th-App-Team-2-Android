package com.teambrake.brake.domain.repository

import com.teambrake.brake.core.model.app.App
import kotlinx.coroutines.flow.Flow

interface AppRepository {

	suspend fun insertApp(parentGroupId: Long, app: App): Result<Unit>

	suspend fun insertApps(parentGroupId: Long, apps: List<App>): Result<Unit>

	fun observeApp(): Flow<List<App>>

	suspend fun getAppGroupIdByPackage(packageName: String): Long?

	suspend fun deleteAppByParentGroupId(parentGroupId: Long): Result<Unit>

	suspend fun clearApps(): Result<Unit>
}
