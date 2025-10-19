package com.teambrake.brake.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.teambrake.brake.core.database.BrakeDatabase
import java.time.LocalDateTime

@Entity(
	tableName = BrakeDatabase.SNOOZE_TABLE_NAME,
	foreignKeys = [
		ForeignKey(
			entity = GroupEntity::class,
			parentColumns = [AppGroupEntity.Companion.GROUP_ID],
			childColumns = [AppGroupEntity.Companion.PARENT_GROUP_ID],
			onDelete = ForeignKey.CASCADE,
		),
	],
	indices = [Index(value = [AppGroupEntity.Companion.PARENT_GROUP_ID])],
)
data class SnoozeEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val snoozeTime: LocalDateTime,
	val parentGroupId: Long,
)
