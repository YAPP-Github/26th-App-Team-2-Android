package com.teambrake.brake.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.teambrake.brake.core.database.BrakeDatabase
import com.teambrake.brake.core.database.BrakeDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

	@Provides
	@Singleton
	fun databaseCallBack(): RoomDatabase.Callback = object : RoomDatabase.Callback() {
		override fun onCreate(db: SupportSQLiteDatabase) {
			super.onCreate(db)
		}
	}

	@Provides
	@Singleton
	fun providesAppDatabase(
		@ApplicationContext context: Context,
		addCallback: RoomDatabase.Callback,
	): BrakeDatabase = Room.databaseBuilder(context, BrakeDatabase::class.java, DATABASE_NAME)
		.addCallback(addCallback)
		.build()
}
