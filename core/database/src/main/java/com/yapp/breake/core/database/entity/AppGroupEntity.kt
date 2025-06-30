package com.yapp.breake.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppGroupEntity(
	@Embedded val group: GroupEntity,
	@Relation(
		parentColumn = GROUP_ID,
		entityColumn = PARENT_GROUP_ID,
	)
	val apps: List<AppEntity>,
	@Relation(
		parentColumn = GROUP_ID,
		entityColumn = PARENT_GROUP_ID,
	)
	val snoozes: List<SnoozeEntity>,
) {

	companion object {
		const val GROUP_ID = "groupId"
		const val PARENT_GROUP_ID = "parentGroupId"
	}
}
