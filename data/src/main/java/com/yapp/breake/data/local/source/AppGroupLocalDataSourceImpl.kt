package com.yapp.breake.data.local.source

import com.yapp.breake.core.appscanner.InstalledAppScanner
import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import com.yapp.breake.data.mapper.toAppGroup
import com.yapp.breake.data.mapper.toGroupEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

internal class AppGroupLocalDataSourceImpl @Inject constructor(
	private val appGroupDao: AppGroupDao,
	private val appScanner: InstalledAppScanner,
) : AppGroupLocalDataSource {

	override suspend fun insertAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.insertAppGroup(appGroup.toGroupEntity())
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 저장에 실패했습니다"))
		}
	}

	override suspend fun getAvailableMinGroupId(
		onError: suspend (Throwable) -> Unit,
	): Long {
		return try {
			appGroupDao.getAvailableMinGroupId()
		} catch (e: Exception) {
			onError(Throwable("사용 가능한 그룹 ID를 가져오는데 실패했습니다"))
			-1L
		}
	}

	override suspend fun deleteAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.deleteAppGroupById(groupId)
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 삭제에 실패했습니다"))
		}
	}

	override fun observeAppGroup(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<AppGroup>> {
		return appGroupDao.observeAppGroup()
			.map { appGroupEntities ->
				appGroupEntities.map { it.toAppGroup(appScanner) }
			}
			.catch { onError(Throwable("앱 그룹 목록 관찰에 실패했습니다")) }
	}

	override suspend fun getAppGroup(
		onError: suspend (Throwable) -> Unit,
	): List<AppGroup> {
		return try {
			appGroupDao.getAppGroup().map { it.toAppGroup(appScanner) }
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 목록을 가져오는데 실패했습니다"))
			emptyList()
		}
	}

	override suspend fun getAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	): AppGroup? {
		return try {
			appGroupDao.getAppGroupById(groupId)?.toAppGroup(appScanner)
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 정보를 가져오는데 실패했습니다"))
			null
		}
	}

	override suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		startTime: LocalDateTime?,
		endTime: LocalDateTime?,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.updateAppGroupState(
				groupId = groupId,
				appGroupState = appGroupState,
				startTime = startTime,
				endTime = endTime,
			)
		} catch (e: Exception) {
			onError(Throwable("앱 그룹 상태 업데이트에 실패했습니다"))
		}
	}

	override suspend fun insertSnooze(
		parentGroupId: Long,
		snoozeTime: LocalDateTime,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.insertSnooze(
				parentGroupId = parentGroupId,
				snoozeTime = snoozeTime,
			)
		} catch (e: Exception) {
			onError(Throwable("스누즈 설정에 실패했습니다"))
		}
	}

	override suspend fun resetSnooze(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.resetSnooze(groupId)
		} catch (e: Exception) {
			onError(Throwable("스누즈 초기화에 실패했습니다"))
		}
	}
}
