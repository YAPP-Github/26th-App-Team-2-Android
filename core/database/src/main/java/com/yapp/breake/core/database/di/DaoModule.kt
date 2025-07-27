package com.yapp.breake.core.database.di

import com.yapp.breake.core.database.BreakeDatabase
import com.yapp.breake.core.database.dao.AppDao
import com.yapp.breake.core.database.dao.AppGroupDao
import com.yapp.breake.core.database.dao.SnoozeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

	@Provides
	@Singleton
	fun provideAppDao(database: BreakeDatabase): AppDao = database.appDao()

	@Provides
	@Singleton
	fun provideSnoozeDao(database: BreakeDatabase): SnoozeDao = database.snoozeDao()

	@Provides
	@Singleton
	fun provideAppGroupDao(database: BreakeDatabase): AppGroupDao = database.appGroupDao()
}
