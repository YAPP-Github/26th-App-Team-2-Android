package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.yapp.breake.core.database.BreakeDatabase.Companion.APP_TABLE_NAME
import com.yapp.breake.core.database.entity.AppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(entity: AppEntity)

	@Query("SELECT * FROM $APP_TABLE_NAME")
	fun observeManagedApps(): Flow<List<AppEntity>>

	@Query("SELECT * FROM $APP_TABLE_NAME")
	suspend fun getManagedApps(): List<AppEntity>
}
