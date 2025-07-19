package com.yapp.breake.domain.repository

import com.yapp.breake.core.model.app.AppGroup
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.Flow

interface AppGroupRepository {

	fun observeAppGroup(): Flow<List<AppGroup>>

	suspend fun getAppGroup(): List<AppGroup>

	suspend fun getAppGroupById(groupId: Long): AppGroup?

	suspend fun setAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
	): Result<Unit>
}
