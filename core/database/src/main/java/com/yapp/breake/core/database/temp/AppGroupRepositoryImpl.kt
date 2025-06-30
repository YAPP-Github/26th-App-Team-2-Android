package com.yapp.breake.core.database.temp

import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.model.app.AppGroup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupDao: AppGroupDao,
) : AppGroupRepository {

	override fun observeAppGroup(): Flow<List<AppGroup>> {
		return appGroupDao.observeAppGroup().map {
			it.map(AppGroupEntity::toAppGroup)
		}
	}

	override suspend fun getAppGroup(): List<AppGroup> {
		return appGroupDao.getAppGroup().map(AppGroupEntity::toAppGroup)
	}

	override suspend fun getAppGroupById(groupId: Long): AppGroup? {
		return appGroupDao.getAppGroupById(groupId)?.toAppGroup()
	}
}
