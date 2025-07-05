package com.yapp.breake.data.di

import com.yapp.breake.data.repositoryImpl.AppGroupRepositoryImpl
import com.yapp.breake.data.repositoryImpl.AppRepositoryImpl
import com.yapp.breake.domain.repository.AppGroupRepository
import com.yapp.breake.domain.repository.AppRepository
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

	@Binds
	@Singleton
	abstract fun bindAppRepository(
		appRepositoryImpl: AppRepositoryImpl,
	): AppRepository
}
