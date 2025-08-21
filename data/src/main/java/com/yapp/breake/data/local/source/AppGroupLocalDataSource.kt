package com.yapp.breake.data.local.source

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface AppGroupLocalDataSource {

	suspend fun insertAppGroup(
		appGroup: AppGroup,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun insertAppGroups(
		appGroups: List<AppGroup>,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun isAppGroupExists(
		groupId: Long,
		onError: suspend (Throwable) -> Unit = {},
	): Boolean

	suspend fun getAvailableMinGroupId(
		onError: suspend (Throwable) -> Unit = {},
	): Long

	suspend fun deleteAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit = {},
	)

	fun observeAppGroup(
		onError: suspend (Throwable) -> Unit = {},
	): Flow<List<AppGroup>>

	suspend fun getAppGroup(
		onError: suspend (Throwable) -> Unit = {},
	): List<AppGroup>

	suspend fun getAppGroupById(
		groupId: Long,
		onError: suspend (Throwable) -> Unit = {},
	): AppGroup?

	suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		startTime: LocalDateTime?,
		endTime: LocalDateTime?,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun updateGroupSessionInfo(
		groupId: Long,
		goalMinutes: Int?,
		sessionStartTime: LocalDateTime?,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun insertSnooze(
		parentGroupId: Long,
		snoozeTime: LocalDateTime,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun resetSnooze(
		groupId: Long,
		onError: suspend (Throwable) -> Unit = {},
	)

	suspend fun clearAppGroup(
		onError: suspend (Throwable) -> Unit = {},
	)
}
