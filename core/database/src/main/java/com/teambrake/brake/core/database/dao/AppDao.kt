package com.teambrake.brake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.teambrake.brake.core.database.BrakeDatabase.Companion.APP_TABLE_NAME
import com.teambrake.brake.core.database.entity.AppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insert(entity: AppEntity)

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertAll(entities: List<AppEntity>)

	@Query("SELECT * FROM $APP_TABLE_NAME")
	fun observeApps(): Flow<List<AppEntity>>

	@Query("SELECT * FROM $APP_TABLE_NAME")
	suspend fun getManagedApps(): List<AppEntity>

	@Query("SELECT * FROM $APP_TABLE_NAME WHERE packageName = :packageName")
	suspend fun getAppByPackageName(packageName: String): AppEntity?

	@Query("DELETE FROM $APP_TABLE_NAME WHERE parentGroupId = :parentGroupId")
	suspend fun deleteAppsByParentGroupId(parentGroupId: Long)

	@Query("DELETE FROM $APP_TABLE_NAME")
	suspend fun clearApps()
}
