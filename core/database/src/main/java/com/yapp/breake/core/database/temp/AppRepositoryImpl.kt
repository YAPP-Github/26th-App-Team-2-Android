package com.yapp.breake.core.database.temp

import com.yapp.breake.core.database.dao.AppDao
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	private val appDao: AppDao
) : AppRepository {

	override suspend fun getAppGroupIdByPackage(packageName: String): Long? {
		return appDao.getAppByPackageName(packageName)?.parentGroupId
	}
}
