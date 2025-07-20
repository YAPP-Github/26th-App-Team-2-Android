package com.yapp.breake.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.database.BreakeDatabase.Companion.DATABASE_NAME
import com.yapp.breake.core.model.app.AppGroupState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

	@Provides
	@Singleton
	fun databaseCallBack(): RoomDatabase.Callback = object : RoomDatabase.Callback() {
		override fun onCreate(db: SupportSQLiteDatabase) {
			super.onCreate(db)

			Executors.newSingleThreadExecutor().execute {
				runBlocking {
					db.execSQL(
						"""
						INSERT INTO `group` (groupId, name, appGroupState) VALUES
						(1, 'YouTube', '${AppGroupState.NeedSetting.name}'),
						""".trimIndent(),
					)

					db.execSQL(
						"""
						INSERT INTO `app` (packageName, category, parentGroupId) VALUES
						('com.google.android.youtube', 'Entertainment', 1),
						""".trimIndent(),
					)
				}
			}
		}
	}

	@Provides
	@Singleton
	fun providesAppDatabase(
		@ApplicationContext context: Context,
		addCallback: RoomDatabase.Callback,
	): BreakeDatabase = Room.databaseBuilder(context, BreakeDatabase::class.java, DATABASE_NAME)
		.addCallback(addCallback)
		.build()
}
