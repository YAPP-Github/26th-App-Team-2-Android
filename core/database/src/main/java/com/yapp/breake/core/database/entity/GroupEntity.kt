package com.yapp.breake.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.model.app.AppGroupState
import java.time.LocalDateTime

@Entity(tableName = BreakeDatabase.GROUP_TABLE_NAME)
data class GroupEntity(
	@PrimaryKey(autoGenerate = true) val groupId: Long = 0,
	val name: String,
	val appGroupState: AppGroupState,
	val startTime: LocalDateTime?,
	val endTime: LocalDateTime?,
)
