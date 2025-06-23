package com.yapp.breake.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yapp.breake.core.database.BreakeDatabase

@Entity(tableName = BreakeDatabase.MANAGED_APP_TABLE_NAME)
data class ManagedAppEntity(
    @PrimaryKey val packageName: String,
	val isBlocking: Boolean,
	val snoozeCount: Int
)
