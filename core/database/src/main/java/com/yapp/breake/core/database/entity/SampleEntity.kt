package com.yapp.breake.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yapp.breake.core.database.BreakeDatabase

@Entity(tableName = BreakeDatabase.SAMPLE_TABLE_NAME)
data class SampleEntity(
    @PrimaryKey val id: Long,
    val parentId: Long,
)
