package com.yapp.breake.data.repositoryImpl

import com.yapp.breake.core.model.app.App
import com.yapp.breake.data.local.source.AppLocalDataSource
import com.yapp.breake.domain.repository.AppRepository
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

	override fun observeApp(): Flow<List<App>> {
		return appLocalDataSource.observeApp()
	}

	override suspend fun getAppGroupIdByPackage(packageName: String): Long? {
		return appLocalDataSource.getAppGroupIdByPackage(packageName = packageName)
	}

	override suspend fun deleteAppByParentGroupId(parentGroupId: Long) {
		appLocalDataSource.deleteAppByParentGroupId(parentGroupId = parentGroupId)
	}

	override suspend fun clearApps() {
		appLocalDataSource.clearApps()
	}
}
