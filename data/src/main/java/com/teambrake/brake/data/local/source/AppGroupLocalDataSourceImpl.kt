package com.teambrake.brake.data.local.source

import com.teambrake.brake.core.appscanner.InstalledAppScanner
import com.teambrake.brake.core.database.dao.AppGroupDao
import com.teambrake.brake.core.model.app.AppGroup
import com.teambrake.brake.core.model.app.AppGroupState
import com.teambrake.brake.data.mapper.toAppList
import com.teambrake.brake.data.mapper.toGroupEntity
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
		} catch (_: Exception) {
			onError(Throwable("앱 그룹 저장에 실패했습니다"))
		}
	}

	override suspend fun insertAppGroups(
		appGroups: List<AppGroup>,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			if (appGroups.isEmpty()) return
			appGroupDao.insertAppGroups(appGroups.map(AppGroup::toGroupEntity))
		} catch (_: Exception) {
			onError(Throwable("앱 그룹 일괄 저장에 실패했습니다"))
		}
	}

	override suspend fun isAppGroupExists(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	): Boolean = try {
		appGroupDao.isAppGroupExists(groupId)
	} catch (_: Exception) {
		onError(Throwable("앱 그룹 존재 여부 확인에 실패했습니다"))
		false
	}

	override suspend fun getAvailableMinGroupId(
		onError: suspend (Throwable) -> Unit,
	): Long = try {
		appGroupDao.getAvailableMinGroupId()
	} catch (_: Exception) {
		onError(Throwable("사용 가능한 그룹 ID를 가져오는데 실패했습니다"))
		-1L
	}

	override suspend fun deleteAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.deleteAppGroupById(groupId)
		} catch (_: Exception) {
			onError(Throwable("앱 그룹 삭제에 실패했습니다"))
		}
	}

	override fun observeAppGroup(
		onError: suspend (Throwable) -> Unit,
	): Flow<List<AppGroup>> = appGroupDao.observeAppGroup()
		.map { appGroupEntities ->
			appGroupEntities.map { it.toAppList(appScanner) }
		}
		.catch { _ ->
			onError(Throwable("앱 그룹 목록 관찰에 실패했습니다"))
			// 예외 발생 시 빈 리스트를 emit 하여 downstream이 .first() 등에서 예외를 던지지 않도록 빈 리스트 방출
			emit(emptyList())
		}

	override suspend fun getAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	): AppGroup? = try {
		appGroupDao.getAppGroupById(groupId)?.toAppList(appScanner)
	} catch (_: Exception) {
		onError(Throwable("앱 그룹 정보를 가져오는데 실패했습니다"))
		null
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
		} catch (_: Exception) {
			onError(Throwable("앱 그룹 상태 업데이트에 실패했습니다"))
		}
	}

	override suspend fun updateGroupSessionInfo(
		groupId: Long,
		goalMinutes: Int?,
		sessionStartTime: LocalDateTime?,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.updateGroupSessionInfo(
				groupId = groupId,
				goalMinutes = goalMinutes,
				sessionStartTime = sessionStartTime,
			)
		} catch (_: Exception) {
			onError(Throwable("그룹 세션 정보 업데이트에 실패했습니다"))
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
		} catch (_: Exception) {
			onError(Throwable("스누즈 설정에 실패했습니다"))
		}
	}

	override suspend fun resetSnooze(
		groupId: Long,
		onError: suspend (Throwable) -> Unit,
	) {
		try {
			appGroupDao.resetSnooze(groupId)
		} catch (_: Exception) {
			onError(Throwable("스누즈 초기화에 실패했습니다"))
		}
	}

	override suspend fun clearAppGroup(onError: suspend (Throwable) -> Unit) {
		try {
			appGroupDao.clearAppGroup()
		} catch (_: Exception) {
			onError(Throwable("앱 그룹 초기화에 실패했습니다"))
		}
	}
}
