package com.yapp.breake.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yapp.breake.core.database.dao.ManagedAppDao
import com.yapp.breake.core.database.entity.ManagedAppEntity

@Database(
    entities = [
        ManagedAppEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
//@TypeConverters(
//    value = [],
//)
internal abstract class BreakeDatabase : RoomDatabase() {

	abstract fun managedAppDao(): ManagedAppDao

    companion object {
        const val DATABASE_NAME = "breake_database"

        const val MANAGED_APP_TABLE_NAME = "managed_app"
    }
}
