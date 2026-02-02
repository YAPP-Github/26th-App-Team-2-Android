package com.teambrake.brake.data.repository

import com.teambrake.brake.core.model.app.App
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	private val appLocalDataSource: AppLocalDataSource,
) : AppRepository {

	override suspend fun insertApp(parentGroupId: Long, app: App): Result<Unit> = try {
		appLocalDataSource.insertApp(parentGroupId = parentGroupId, app = app)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun insertApps(parentGroupId: Long, apps: List<App>): Result<Unit> = try {
		appLocalDataSource.insertApps(parentGroupId = parentGroupId, apps = apps)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override fun observeApp(): Flow<List<App>> = appLocalDataSource.observeApp()

	override suspend fun getAppGroupIdByPackage(packageName: String): Long? = appLocalDataSource.getAppGroupIdByPackage(packageName = packageName)

	override suspend fun deleteAppByParentGroupId(parentGroupId: Long): Result<Unit> = try {
		appLocalDataSource.deleteAppByParentGroupId(parentGroupId = parentGroupId)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun clearApps(): Result<Unit> = try {
		appLocalDataSource.clearApps()
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}
}
