package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yapp.breake.core.database.entity.AppGroup
import kotlinx.coroutines.flow.Flow

@Dao
interface AppGroupDao {

	@Transaction
	@Query("SELECT * FROM `group`")
	fun observeAppGroup(): Flow<List<AppGroup>>

	@Transaction
	@Query("SELECT * FROM `group`")
	suspend fun getAppGroup(): List<AppGroup>

	@Transaction
	@Query("SELECT * FROM `group` WHERE groupId = :groupId")
	suspend fun getAppGroupById(groupId: Long): AppGroup?

	@Query("DELETE FROM `group` WHERE groupId = :groupId")
	suspend fun deleteAppGroupById(groupId: Long)
}
