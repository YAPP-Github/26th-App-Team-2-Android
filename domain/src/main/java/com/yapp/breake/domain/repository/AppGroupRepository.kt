package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.BlockingState
import kotlinx.coroutines.flow.Flow

interface AppGroupRepository {

	fun observeAppGroup(): Flow<List<AppGroup>>

	suspend fun getAppGroup(): List<AppGroup>

	suspend fun getAppGroupById(groupId: Long): AppGroup?

	suspend fun updateAppGroupState(
		groupId: Long,
		blockingState: BlockingState,
	): Result<Unit>
}
