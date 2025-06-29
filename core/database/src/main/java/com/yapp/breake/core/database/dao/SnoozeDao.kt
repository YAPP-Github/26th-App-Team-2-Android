package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.database.entity.SnoozeEntity

@Dao
interface SnoozeDao {

	@Insert
	suspend fun insertSnooze(snooze: SnoozeEntity)

	@Query("DELETE FROM ${BreakeDatabase.SNOOZE_TABLE_NAME} WHERE parentGroupId = :groupId")
	suspend fun resetSnoozes(groupId: Long)
}
