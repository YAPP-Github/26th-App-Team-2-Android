package com.teambrake.brake.data.local.source.di

import com.teambrake.brake.data.local.source.AppGroupLocalDataSource
import com.teambrake.brake.data.local.source.AppGroupLocalDataSourceImpl
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.data.local.source.AppLocalDataSourceImpl
import com.teambrake.brake.data.local.source.AuthLocalDataSource
import com.teambrake.brake.data.local.source.AuthLocalDataSourceImpl
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSourceImpl
import com.teambrake.brake.data.local.source.NameLocalDataSource
import com.teambrake.brake.data.local.source.NameLocalDataSourceImpl
import com.teambrake.brake.data.local.source.OnboardingLocalDataSource
import com.teambrake.brake.data.local.source.OnboardingLocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalSourceModule {

	@Binds
	@Singleton
	abstract fun bindAuthLocalDataSource(
		authLocalDataSource: AuthLocalDataSourceImpl,
	): AuthLocalDataSource

	@Binds
	@Singleton
	abstract fun bindTokenLocalDataSource(
		tokenLocalDataSource: TokenLocalDataSourceImpl,
	): TokenLocalDataSource

	@Binds
	@Singleton
	abstract fun bindNameLocalDataSource(
		nameLocalDataSource: NameLocalDataSourceImpl,
	): NameLocalDataSource

	@Binds
	@Singleton
	abstract fun bindOnboardingLocalDataSource(
		onboardingLocalDataSourceImpl: OnboardingLocalDataSourceImpl,
	): OnboardingLocalDataSource

	@Binds
	@Singleton
	abstract fun bindAppGroupLocalDataSource(
		appGroupLocalDataSource: AppGroupLocalDataSourceImpl,
	): AppGroupLocalDataSource

	@Binds
	internal abstract fun bindAppLocalDataSource(
		appLocalDataSourceImpl: AppLocalDataSourceImpl,
	): AppLocalDataSource
}
