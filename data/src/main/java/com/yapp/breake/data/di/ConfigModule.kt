package com.yapp.breake.data.di

import com.yapp.breake.data.etc.ConstTimeProviderImpl
import com.yapp.breake.domain.etc.ConstTimeProvider
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
