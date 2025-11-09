package com.teambrake.brake.data.di

import com.teambrake.brake.data.etc.ConstTimeProviderImpl
import com.teambrake.brake.domain.etc.ConstTimeProvider
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
