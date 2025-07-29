package com.yapp.breake.data.di

import com.yapp.breake.data.repositoryImpl.ConstTimeProviderImpl
import com.yapp.breake.domain.repository.ConstTimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ConfigModule {

	@Binds
	abstract fun bindConstTimeProvider(
		impl: ConstTimeProviderImpl,
	): ConstTimeProvider
}
