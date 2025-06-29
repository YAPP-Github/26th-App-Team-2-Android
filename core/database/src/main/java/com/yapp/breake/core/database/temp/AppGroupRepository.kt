package com.yapp.breake.core.database.temp

import com.yapp.breake.core.database.entity.AppGroup
import kotlinx.coroutines.flow.Flow

interface AppGroupRepository {

	fun observeAppGroup(): Flow<List<AppGroup>>

	suspend fun getAppGroup(): List<AppGroup>

	suspend fun getAppGroupById(groupId: Long): AppGroup?
}
