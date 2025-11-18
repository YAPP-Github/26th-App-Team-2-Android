package com.teambrake.brake.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.teambrake.brake.core.database.BrakeDatabase

@Entity(
	tableName = BrakeDatabase.APP_TABLE_NAME,
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
data class AppEntity(
	@PrimaryKey(autoGenerate = true) val id: Long = 0,
	val packageName: String,
	val name: String,
	val category: String,
	val parentGroupId: Long,
)
