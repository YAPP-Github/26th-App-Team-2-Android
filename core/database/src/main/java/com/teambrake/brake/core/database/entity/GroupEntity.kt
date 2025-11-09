package com.teambrake.brake.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.teambrake.brake.core.database.BrakeDatabase
import com.teambrake.brake.core.model.app.AppGroupState
import java.time.LocalDateTime

@Entity(tableName = BrakeDatabase.GROUP_TABLE_NAME)
data class GroupEntity(
	@PrimaryKey val groupId: Long,
	val name: String,
	val appGroupState: AppGroupState,
	val goalMinutes: Int?,
	val sessionStartTime: LocalDateTime?,
	val startTime: LocalDateTime?,
	val endTime: LocalDateTime?,
)
