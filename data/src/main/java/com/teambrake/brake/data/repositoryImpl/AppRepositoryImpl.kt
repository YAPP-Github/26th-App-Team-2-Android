package com.teambrake.brake.data.repositoryImpl

import com.teambrake.brake.core.model.app.App
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	private val appLocalDataSource: AppLocalDataSource,
) : AppRepository {

	override suspend fun insertApp(parentGroupId: Long, app: App) {
		appLocalDataSource.insertApp(parentGroupId = parentGroupId, app = app)
	}

	override suspend fun insertApps(parentGroupId: Long, apps: List<App>) {
		appLocalDataSource.insertApps(parentGroupId = parentGroupId, apps = apps)
	}

	override fun observeApp(): Flow<List<App>> = appLocalDataSource.observeApp()

	override suspend fun getAppGroupIdByPackage(packageName: String): Long? = appLocalDataSource.getAppGroupIdByPackage(packageName = packageName)

	override suspend fun deleteAppByParentGroupId(parentGroupId: Long) {
		appLocalDataSource.deleteAppByParentGroupId(parentGroupId = parentGroupId)
	}

	override suspend fun clearApps() {
		appLocalDataSource.clearApps()
	}
}
