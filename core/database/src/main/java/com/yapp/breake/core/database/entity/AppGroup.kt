package com.yapp.breake.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class AppGroup(
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

	val snoozeCount: Int
		get() = snoozes.size

	companion object {
		const val GROUP_ID = "groupId"
		const val PARENT_GROUP_ID = "parentGroupId"
	}
}
