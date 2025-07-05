package com.yapp.breake.data.repositoryImpl

import com.yapp.breake.core.database.dao.AppDao
import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.model.app.App
import com.yapp.breake.data.mapper.toApp
import com.yapp.breake.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	private val appDao: AppDao,
) : AppRepository {

	override fun observeApp(): Flow<List<App>> {
		return appDao.observeApps().map { appEntities ->
			appEntities.map(AppEntity::toApp)
		}
	}

	override suspend fun getAppGroupIdByPackage(packageName: String): Long? {
		return appDao.getAppByPackageName(packageName)?.parentGroupId
	}
}
