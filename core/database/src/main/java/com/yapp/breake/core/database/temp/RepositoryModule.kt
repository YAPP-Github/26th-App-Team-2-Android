package com.yapp.breake.core.database.temp

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

	@Binds
	@Singleton
	abstract fun bindAppGroupRepository(
		appGroupRepositoryImpl: AppGroupRepositoryImpl,
	): AppGroupRepository
}
