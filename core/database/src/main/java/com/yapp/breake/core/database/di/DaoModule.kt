package com.yapp.breake.core.database.di

import com.yapp.breake.core.database.BreakeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.yapp.breake.core.database.dao.ManagedAppDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideManagedApp(database: BreakeDatabase): ManagedAppDao = database.managedAppDao()
}
