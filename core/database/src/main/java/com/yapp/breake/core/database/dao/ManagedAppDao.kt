package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.yapp.breake.core.database.BreakeDatabase.Companion.MANAGED_APP_TABLE_NAME
import com.yapp.breake.core.database.entity.ManagedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ManagedAppDao {

    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    suspend fun insert(entity: ManagedAppEntity)

    @Query("SELECT * FROM $MANAGED_APP_TABLE_NAME")
    fun observeManagedApps(): Flow<List<ManagedAppEntity>>

    @Query("SELECT * FROM $MANAGED_APP_TABLE_NAME")
    suspend fun getManagedApps(): List<ManagedAppEntity>
}
