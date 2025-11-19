package com.teambrake.brake.data.repository

import com.teambrake.brake.core.detection.CachedDatabase
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.data.local.source.AppGroupLocalDataSource
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.data.remote.source.AppGroupRemoteDataSource
import com.teambrake.brake.data.repository.util.OfflineBlocker
import com.teambrake.brake.data.repository.util.OfflineException
import com.teambrake.brake.domain.repository.AppGroupRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.time.LocalDateTime
import javax.inject.Inject

internal class AppGroupRepositoryImpl @Inject constructor(
	private val offlineBlocker: OfflineBlocker,
	private val appGroupLocalDataSource: AppGroupLocalDataSource,
	private val appGroupRemoteDataSource: AppGroupRemoteDataSource,
	private val appLocalDataSource: AppLocalDataSource,
	private val cachedDatabase: CachedDatabase,
) : AppGroupRepository {

	override suspend fun insertAppGroup(appGroup: AppGroup): AppGroup {
		val isUpdate = appGroupLocalDataSource.isAppGroupExists(appGroup.id)

		return offlineBlocker.blockFlow {
			if (isUpdate) {
				appGroupRemoteDataSource.updateAppGroup(appGroup = appGroup)
			} else {
				appGroupRemoteDataSource.createAppGroup(appGroup = appGroup)
			}
		}.map { resultGroup ->
			appGroupLocalDataSource.insertAppGroup(resultGroup)
			if (isUpdate) {
				cachedDatabase.updateAppGroupInCache(resultGroup)
			} else {
				cachedDatabase.addAppGroupToCache(resultGroup)
			}
			resultGroup
		}.catch {
			if (it is OfflineException) {
				appGroupLocalDataSource.insertAppGroup(appGroup)
				if (isUpdate) {
					cachedDatabase.updateAppGroupInCache(appGroup)
				} else {
					cachedDatabase.addAppGroupToCache(appGroup)
				}
				emit(appGroup)
			} else {
				throw it
			}
		}.first()
	}

	override suspend fun getAvailableMinGroupId(): Long =
		appGroupLocalDataSource.getAvailableMinGroupId()

	override suspend fun deleteAppGroupByGroupId(groupId: Long) {
		try {
			offlineBlocker.block {
				appGroupRemoteDataSource.deleteAppGroup(
					groupId = groupId,
					onSuccess = {
						appGroupLocalDataSource.deleteAppGroupById(groupId = groupId)
						cachedDatabase.removeAppGroupFromCache(groupId)
					},
				)
			}
		} catch (_: OfflineException) {
			// 오프라인 모드, 원격 앱 그룹 삭제 스킵
		} catch (_: Exception) {
			// 기타 예외는 무시
		} finally {
			// 항상 로컬에서 앱 그룹 삭제 및 캐시 제거를 수행 (네트워크 상태와 관계없이 실행)
			appGroupLocalDataSource.deleteAppGroupById(groupId = groupId)
			cachedDatabase.removeAppGroupFromCache(groupId)
		}
	}

	override suspend fun clearAppGroup() {
		appGroupLocalDataSource.clearAppGroup()
		cachedDatabase.clearCache()
	}

	override fun observeAppGroup(): Flow<List<AppGroup>> =
		appGroupLocalDataSource.observeAppGroup()
			.onStart {
				val localList = appGroupLocalDataSource.observeAppGroup().firstOrNull() ?: emptyList()
				if (localList.isEmpty()) {
					runCatching {
						offlineBlocker.blockFlow { appGroupRemoteDataSource.getAppGroups() }
							.collect { remoteList ->
								appGroupLocalDataSource.insertAppGroups(remoteList)
								remoteList.forEach { appLocalDataSource.insertApps(it.id, it.apps) }
								cachedDatabase.initializeCachedState(remoteList)
							}
					}
				}
			}
			.onEach { cachedDatabase.initializeCachedState(it) }

	override suspend fun getAppGroupById(groupId: Long): AppGroup? =
		appGroupLocalDataSource.getAppGroupById(groupId = groupId)

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
	): Result<Unit> = try {
		appGroupLocalDataSource.updateGroupSessionInfo(
			groupId = groupId,
			goalMinutes = goalMinutes,
			sessionStartTime = sessionStartTime,
		)
		Result.success(Unit)
	} catch (e: Exception) {
		Result.failure(e)
	}

	override suspend fun insertSnooze(groupId: Long): Result<Unit> = try {
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

	override suspend fun resetSnooze(groupId: Long): Result<Unit> = try {
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
