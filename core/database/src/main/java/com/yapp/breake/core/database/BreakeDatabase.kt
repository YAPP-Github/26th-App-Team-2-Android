package com.yapp.breake.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yapp.breake.core.database.dao.SampleDao
import com.yapp.breake.core.database.entity.SampleEntity

@Database(
	entities = [
		SampleEntity::class,
	],
	version = 1,
	exportSchema = true,
)
//@TypeConverters(
//    value = [],
//)
internal abstract class BreakeDatabase : RoomDatabase() {

	abstract fun sampleDao(): SampleDao

	companion object {
		const val DATABASE_NAME = "breake_database"

		const val SAMPLE_TABLE_NAME = "sample"
	}
}
