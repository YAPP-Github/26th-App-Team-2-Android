package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface AppGroupDao {

	@Transaction
	@Query("SELECT * FROM `group_table`")
	fun observeAppGroup(): Flow<List<AppGroupEntity>>

	@Transaction
	@Query("SELECT * FROM `group_table`")
	suspend fun getAppGroup(): List<AppGroupEntity>

	@Transaction
	@Query("SELECT * FROM `group_table` WHERE groupId = :groupId")
	suspend fun getAppGroupById(groupId: Long): AppGroupEntity?

	@Query("DELETE FROM `group_table` WHERE groupId = :groupId")
	suspend fun deleteAppGroupById(groupId: Long)

	@Query("UPDATE `group_table` SET appGroupState = :appGroupState WHERE groupId = :groupId")
	suspend fun updateAppGroupState(groupId: Long, appGroupState: AppGroupState)

	@Query(
		"INSERT INTO `snooze_table` (parentGroupId, snoozeTime) VALUES (:parentGroupId, :snoozeTime)",
	)
	suspend fun insertSnooze(parentGroupId: Long, snoozeTime: LocalDateTime)
}
