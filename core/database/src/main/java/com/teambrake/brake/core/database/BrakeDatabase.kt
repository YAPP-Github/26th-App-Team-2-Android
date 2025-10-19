package com.teambrake.brake.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.teambrake.brake.core.database.converter.AppGroupStateConverter
import com.teambrake.brake.core.database.converter.LocalDateTimeConverter
import com.teambrake.brake.core.database.dao.AppDao
import com.teambrake.brake.core.database.dao.AppGroupDao
import com.teambrake.brake.core.database.dao.SnoozeDao
import com.teambrake.brake.core.database.entity.AppEntity
import com.teambrake.brake.core.database.entity.GroupEntity
import com.teambrake.brake.core.database.entity.SnoozeEntity

@Database(
	entities = [
		AppEntity::class,
		SnoozeEntity::class,
		GroupEntity::class,
	],
	autoMigrations = [],
	version = 1,
	exportSchema = true,
)
@TypeConverters(
	value = [
		AppGroupStateConverter::class,
		LocalDateTimeConverter::class,
	],
)
internal abstract class BrakeDatabase : RoomDatabase() {

	abstract fun appGroupDao(): AppGroupDao
	abstract fun appDao(): AppDao
	abstract fun snoozeDao(): SnoozeDao

	companion object {
		const val DATABASE_NAME = "brake_database"

		const val GROUP_TABLE_NAME = "group_table"
		const val APP_TABLE_NAME = "app_table"
		const val SNOOZE_TABLE_NAME = "snooze_table"
	}
}
