package com.yapp.breake.core.database.temp

import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.database.entity.AppGroup
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupDao: AppGroupDao,
) : AppGroupRepository {

	override fun observeAppGroup(): Flow<List<AppGroup>> {
		return appGroupDao.observeAppGroup()
	}

	override suspend fun getAppGroup(): List<AppGroup> {
		return appGroupDao.getAppGroup()
	}

	override suspend fun getAppGroupById(groupId: Long): AppGroup? {
		return appGroupDao.getAppGroupById(groupId)
	}
}
