package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface AppGroupRepository {

	fun observeAppGroup(): Flow<List<AppGroup>>

	suspend fun getAppGroup(): List<AppGroup>

	suspend fun getAppGroupById(groupId: Long): AppGroup?

	suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		endTime: LocalDateTime?,
	): Result<Unit>

	suspend fun insertSnooze(
		groupId: Long,
	): Result<Unit>

	suspend fun resetSnooze(
		groupId: Long,
	): Result<Unit>
}
