package com.teambrake.brake.core.database.di

import com.teambrake.brake.core.database.BrakeDatabase
import com.teambrake.brake.core.database.dao.AppDao
import com.teambrake.brake.core.database.dao.AppGroupDao
import com.teambrake.brake.core.database.dao.SnoozeDao
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
	fun provideAppDao(database: BrakeDatabase): AppDao = database.appDao()

	@Provides
	@Singleton
	fun provideSnoozeDao(database: BrakeDatabase): SnoozeDao = database.snoozeDao()

	@Provides
	@Singleton
	fun provideAppGroupDao(database: BrakeDatabase): AppGroupDao = database.appGroupDao()
}
