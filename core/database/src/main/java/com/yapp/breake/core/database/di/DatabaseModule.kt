package com.yapp.breake.core.database.di

import android.content.Context
import androidx.room.Room
import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.database.BreakeDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

//    @Provides
//    @Singleton
//    fun databaseCallBack(): RoomDatabase.Callback = object : RoomDatabase.Callback() {
//        override fun onCreate(db: SupportSQLiteDatabase) {
//            super.onCreate(db)
//
//            Executors.newSingleThreadExecutor().execute {
//                runBlocking {
//                    db.execSQL(SpendingPlanEntity.INSERT_QUERY)
//                }
//            }
//        }
//    }

	@Provides
	@Singleton
	fun providesAppDatabase(
		@ApplicationContext context: Context,
//        addCallback: RoomDatabase.Callback
	): BreakeDatabase = Room.databaseBuilder(context, BreakeDatabase::class.java, DATABASE_NAME)
//        .addCallback(addCallback)
		.build()
}
