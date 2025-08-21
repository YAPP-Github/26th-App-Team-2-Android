package com.yapp.breake.data.repository

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.data.local.source.AppGroupLocalDataSource
import com.yapp.breake.data.remote.source.AppGroupRemoteDataSource
import com.yapp.breake.domain.repository.AppGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

internal class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupLocalDataSource: AppGroupLocalDataSource,
	private val appGroupRemoteDataSource: AppGroupRemoteDataSource,
) : AppGroupRepository {

	override suspend fun insertAppGroup(appGroup: AppGroup): AppGroup {
		return if (appGroupLocalDataSource.isAppGroupExists(appGroup.id)) {
			appGroupRemoteDataSource.updateAppGroup(
				appGroup = appGroup,
			).map { updatedGroup ->
				appGroupLocalDataSource.insertAppGroup(updatedGroup)

				updatedGroup
			}.first()
		} else {
			appGroupRemoteDataSource.createAppGroup(
				appGroup = appGroup,
			).map { newGroup ->
				val appsWithOriginalPackages = newGroup.apps.map { newApp ->
					val originalApp = appGroup.apps.find { it.name == newApp.name }
					if (originalApp != null) {
						newApp.copy(packageName = originalApp.packageName)
					} else {
						newApp
					}
				}
				val groupWithOriginalPackages = newGroup.copy(apps = appsWithOriginalPackages)
				appGroupLocalDataSource.insertAppGroup(groupWithOriginalPackages)

				groupWithOriginalPackages
			}.first()
		}
	}

	override suspend fun getAvailableMinGroupId(): Long =
		appGroupLocalDataSource.getAvailableMinGroupId()

	override suspend fun deleteAppGroupByGroupId(groupId: Long) {
		appGroupRemoteDataSource.deleteAppGroup(
			groupId = groupId,
			onSuccess = {
				appGroupLocalDataSource.deleteAppGroupById(groupId = groupId)
			},
		)
	}

	override suspend fun clearAppGroup() {
		appGroupLocalDataSource.clearAppGroup()
	}

	override fun observeAppGroup(): Flow<List<AppGroup>> {
		return appGroupLocalDataSource.observeAppGroup()
			.onEach { localList ->
				if (localList.isEmpty()) {
					appGroupRemoteDataSource.getAppGroups().collect { remoteList ->
						appGroupLocalDataSource.insertAppGroups(remoteList)
					}
				}
			}
	}

	override suspend fun getAppGroup(): List<AppGroup> {
		val local = appGroupLocalDataSource.getAppGroup()
		if (local.isNotEmpty()) return local
		val remoteList = appGroupRemoteDataSource.getAppGroups().firstOrNull() ?: emptyList()
		appGroupLocalDataSource.insertAppGroups(remoteList)
		return remoteList
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

	override suspend fun updateGroupSessionInfo(
		groupId: Long,
		goalMinutes: Int?,
		sessionStartTime: LocalDateTime?,
	): Result<Unit> {
		return try {
			appGroupLocalDataSource.updateGroupSessionInfo(
				groupId = groupId,
				goalMinutes = goalMinutes,
				sessionStartTime = sessionStartTime,
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
