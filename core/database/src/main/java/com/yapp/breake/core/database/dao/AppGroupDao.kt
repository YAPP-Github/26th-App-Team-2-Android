package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yapp.breake.core.database.entity.AppGroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppGroupDao {

	@Transaction
	@Query("SELECT * FROM `group`")
	fun observeAppGroup(): Flow<List<AppGroupEntity>>

	@Transaction
	@Query("SELECT * FROM `group`")
	suspend fun getAppGroup(): List<AppGroupEntity>

	@Transaction
	@Query("SELECT * FROM `group` WHERE groupId = :groupId")
	suspend fun getAppGroupById(groupId: Long): AppGroupEntity?

	@Query("DELETE FROM `group` WHERE groupId = :groupId")
	suspend fun deleteAppGroupById(groupId: Long)

	@Query("UPDATE `group` SET blockingState = :blockingState WHERE groupId = :groupId")
	suspend fun updateAppGroupState(groupId: Long, blockingState: String)
}
