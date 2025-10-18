package com.teambrake.brake.data.repository

import com.teambrake.brake.core.detection.CachedDatabase
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.data.local.source.AppGroupLocalDataSource
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.data.remote.source.AppGroupRemoteDataSource
import com.teambrake.brake.domain.repository.AppGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import java.time.LocalDateTime
import javax.inject.Inject

internal class AppGroupRepositoryImpl @Inject constructor(
	private val appGroupLocalDataSource: AppGroupLocalDataSource,
	private val appGroupRemoteDataSource: AppGroupRemoteDataSource,
	private val appLocalDataSource: AppLocalDataSource,
	private val cachedDatabase: CachedDatabase,
) : AppGroupRepository {

	override suspend fun insertAppGroup(appGroup: AppGroup): AppGroup {
		return if (appGroupLocalDataSource.isAppGroupExists(appGroup.id)) {
			appGroupRemoteDataSource.updateAppGroup(
				appGroup = appGroup,
			).map { updatedGroup ->
				appGroupLocalDataSource.insertAppGroup(updatedGroup)
				// API 성공 후에 캐시 업데이트
				cachedDatabase.updateAppGroupInCache(updatedGroup)
				updatedGroup
			}
		} else {
			appGroupRemoteDataSource.createAppGroup(
				appGroup = appGroup,
			).map { newGroup ->
				appGroupLocalDataSource.insertAppGroup(newGroup)
				// API 성공 후에 캐시에 추가
				cachedDatabase.addAppGroupToCache(newGroup)
				newGroup
			}
		}.first()
	}

	override suspend fun getAvailableMinGroupId(): Long =
		appGroupLocalDataSource.getAvailableMinGroupId()

	override suspend fun deleteAppGroupByGroupId(groupId: Long) {
		appGroupRemoteDataSource.deleteAppGroup(
			groupId = groupId,
			onSuccess = {
				appGroupLocalDataSource.deleteAppGroupById(groupId = groupId)
				cachedDatabase.removeAppGroupFromCache(groupId)
			},
		)
	}

	override suspend fun clearAppGroup() {
		appGroupLocalDataSource.clearAppGroup()
		cachedDatabase.clearCache()
	}

	override fun observeAppGroup(): Flow<List<AppGroup>> {
		return appGroupLocalDataSource.observeAppGroup()
			.onEach { localList ->
				if (localList.isEmpty()) {
					appGroupRemoteDataSource.getAppGroups().collect { remoteList ->
						appGroupLocalDataSource.insertAppGroups(remoteList)
						remoteList.forEach {
							appLocalDataSource.insertApps(it.id, it.apps)
						}
						// 원격에서 가져온 데이터로 캐시 초기화
						cachedDatabase.initializeCachedState(remoteList)
					}
				} else {
					// DB 변경 시마다 캐시 업데이트
					cachedDatabase.initializeCachedState(localList)
				}
			}
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
		cachedDatabase.updateCachedState(groupId = groupId, appGroupState = appGroupState)
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
			// Snooze 삽입 후 캐시의 snoozesCount 업데이트
			val updatedGroup = appGroupLocalDataSource.getAppGroupById(groupId)
			updatedGroup?.let {
				cachedDatabase.updateSnoozeCount(groupId, it.snoozesCount)
			}
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
			// Snooze 리셋 후 캐시의 snoozesCount를 0으로 업데이트
			cachedDatabase.updateSnoozeCount(groupId, 0)
			Result.success(Unit)
		} catch (e: Exception) {
			Result.failure(e)
		}
	}
}
