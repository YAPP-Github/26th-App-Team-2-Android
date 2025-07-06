package com.yapp.breake.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yapp.breake.core.database.converter.BlockingStateConverter
import com.yapp.breake.core.database.converter.LocalDateTimeConverter
import com.yapp.breake.core.database.dao.AppDao
import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.database.dao.SnoozeDao
import com.yapp.breake.core.database.entity.AppEntity
import com.yapp.breake.core.database.entity.GroupEntity
import com.yapp.breake.core.database.entity.SnoozeEntity

@Database(
	entities = [
		AppEntity::class,
		SnoozeEntity::class,
		GroupEntity::class,
	],
	version = 1,
	exportSchema = true,
)
@TypeConverters(
	value = [
		BlockingStateConverter::class,
		LocalDateTimeConverter::class,
	],
)
internal abstract class BreakeDatabase : RoomDatabase() {

	abstract fun appGroupDao(): AppGroupDao
	abstract fun appDao(): AppDao
	abstract fun snoozeDao(): SnoozeDao

	companion object {
		const val DATABASE_NAME = "breake_database"

		const val GROUP_TABLE_NAME = "group"
		const val APP_TABLE_NAME = "app"
		const val SNOOZE_TABLE_NAME = "snooze"
	}
}
