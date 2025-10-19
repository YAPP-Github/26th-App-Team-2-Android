package com.teambrake.brake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.teambrake.brake.core.database.BrakeDatabase
import com.teambrake.brake.core.database.entity.SnoozeEntity

@Dao
interface SnoozeDao {

	@Insert
	suspend fun insertSnooze(snooze: SnoozeEntity)

	@Query("DELETE FROM ${BrakeDatabase.SNOOZE_TABLE_NAME} WHERE parentGroupId = :groupId")
	suspend fun resetSnoozes(groupId: Long)
}
