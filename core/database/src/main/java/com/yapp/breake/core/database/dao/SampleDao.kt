package com.yapp.breake.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.yapp.breake.core.database.BreakeDatabase.Companion.SAMPLE_TABLE_NAME
import com.yapp.breake.core.database.entity.SampleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SampleDao {

	@Upsert
	suspend fun upsert(entity: SampleEntity)

	@Query("SELECT * FROM $SAMPLE_TABLE_NAME")
	fun getListFlow(): Flow<List<SampleEntity>>
}
