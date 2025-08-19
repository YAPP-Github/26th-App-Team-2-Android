package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.yapp.breake.core.database.entity.AppGroupEntity
import com.yapp.breake.core.database.entity.GroupEntity
import com.yapp.breake.core.model.app.AppGroupState
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface AppGroupDao {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertAppGroup(groupEntity: GroupEntity)

	@Query(
		"""
		SELECT CASE
			WHEN NOT EXISTS(SELECT 1 FROM `group_table` WHERE groupId = 1) THEN 1
			ELSE (
				SELECT MIN(t1.groupId + 1)
				FROM `group_table` t1
				LEFT JOIN `group_table` t2 ON t1.groupId + 1 = t2.groupId
				WHERE t2.groupId IS NULL
			)
		END
		""",
	)
	suspend fun getAvailableMinGroupId(): Long

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

	@Query("DELETE FROM `group_table`")
	suspend fun clearAppGroup()

	@Query(
		"UPDATE `group_table` SET " +
			"appGroupState = :appGroupState," +
			" startTime = :startTime," +
			" endTime = :endTime WHERE groupId = :groupId",
	)
	suspend fun updateAppGroupState(
		groupId: Long,
		appGroupState: AppGroupState,
		startTime: LocalDateTime?,
		endTime: LocalDateTime?,
	)

	@Query(
		"INSERT INTO `snooze_table` (parentGroupId, snoozeTime) VALUES (:parentGroupId, :snoozeTime)",
	)
	suspend fun insertSnooze(parentGroupId: Long, snoozeTime: LocalDateTime)

	@Query("DELETE FROM `snooze_table` WHERE parentGroupId = :groupId")
	suspend fun resetSnooze(groupId: Long)
}
