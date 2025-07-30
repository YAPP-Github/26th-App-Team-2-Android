package com.yapp.breake.data.repositoryImpl

import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.data.mapper.toAppGroup
import com.yapp.breake.data.mapper.toGroupEntity
import com.yapp.breake.domain.repository.AppGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupDao: AppGroupDao,
) : AppGroupRepository {
	override suspend fun insertAppGroup(appGroup: AppGroup) {
		appGroupDao.insertAppGroup(
			appGroup.toGroupEntity(),
		)
	}

	override suspend fun deleteAppGroupByGroupId(groupId: Long) {
		appGroupDao.deleteAppGroupById(groupId)
	}

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

	override suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		endTime: LocalDateTime?,
	): Result<Unit> {
		return try {
			appGroupDao.updateAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
				endTime = endTime,
			)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun insertSnooze(groupId: Long): Result<Unit> {
		return try {
			appGroupDao.insertSnooze(
				parentGroupId = groupId,
				snoozeTime = LocalDateTime.now(),
			)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun resetSnooze(groupId: Long): Result<Unit> {
		return try {
			appGroupDao.resetSnooze(groupId)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
