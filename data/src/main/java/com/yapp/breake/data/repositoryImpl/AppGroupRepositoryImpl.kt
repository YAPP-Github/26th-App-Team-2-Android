package com.yapp.breake.data.repositoryImpl

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.data.local.source.AppGroupLocalDataSource
import com.yapp.breake.data.remote.source.AppGroupRemoteDataSource
import com.yapp.breake.domain.repository.AppGroupRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

internal class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupLocalDataSource: AppGroupLocalDataSource,
	private val appGroupRemoteDataSource: AppGroupRemoteDataSource
) : AppGroupRepository {

	override suspend fun insertAppGroup(appGroup: AppGroup) {
		appGroupLocalDataSource.insertAppGroup(appGroup)
	}

	override suspend fun getAvailableMinGroupId(): Long =
		appGroupLocalDataSource.getAvailableMinGroupId()

	override suspend fun deleteAppGroupByGroupId(groupId: Long) {
		appGroupLocalDataSource.deleteAppGroupById(groupId = groupId)
	}

	override suspend fun clearAppGroup() {
		appGroupDao.clearAppGroup()
	}

	override fun observeAppGroup(): Flow<List<AppGroup>> {
		return appGroupLocalDataSource.observeAppGroup()
	}

	override suspend fun getAppGroup(): List<AppGroup> {
		return appGroupLocalDataSource.getAppGroup()
	}

	override suspend fun getAppGroupById(groupId: Long): AppGroup? {
		return appGroupLocalDataSource.getAppGroupById(groupId = groupId)
	}

	override suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		startTime: LocalDateTime?,
		endTime: LocalDateTime?,
	): Result<Unit> {
		return try {
			appGroupLocalDataSource.updateAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
				startTime = startTime,
				endTime = endTime,
			)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}

	override suspend fun insertSnooze(groupId: Long): Result<Unit> {
		return try {
			appGroupLocalDataSource.insertSnooze(
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
			appGroupLocalDataSource.resetSnooze(
				groupId = groupId,
			)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
