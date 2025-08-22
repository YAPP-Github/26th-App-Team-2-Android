package com.yapp.breake.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.database.entity.AppGroupEntity.Companion.GROUP_ID
import com.yapp.breake.core.database.entity.AppGroupEntity.Companion.PARENT_GROUP_ID

@Entity(
	tableName = BreakeDatabase.APP_TABLE_NAME,
	foreignKeys = [
		ForeignKey(
			entity = GroupEntity::class,
			parentColumns = [GROUP_ID],
			childColumns = [PARENT_GROUP_ID],
			onDelete = ForeignKey.CASCADE,
		),
	],
	indices = [Index(value = [PARENT_GROUP_ID])],
)
data class AppEntity(
	@PrimaryKey val id: Long,
	val packageName: String,
	val name: String,
	val category: String,
	val parentGroupId: Long,
)
