package com.teambrake.brake.data.local.source.di

import com.teambrake.brake.data.local.source.AppGroupLocalDataSource
import com.teambrake.brake.data.local.source.AppGroupLocalDataSourceImpl
import com.teambrake.brake.data.local.source.AppLocalDataSource
import com.teambrake.brake.data.local.source.AppLocalDataSourceImpl
import com.teambrake.brake.data.local.source.AuthLocalDataSource
import com.teambrake.brake.data.local.source.AuthLocalDataSourceImpl
import com.teambrake.brake.data.local.source.TokenLocalDataSource
import com.teambrake.brake.data.local.source.TokenLocalDataSourceImpl
import com.teambrake.brake.data.local.source.UserLocalDataSource
import com.teambrake.brake.data.local.source.UserLocalDataSourceImpl
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
	abstract fun bindUserLocalDataSource(
		userLocalDataSource: UserLocalDataSourceImpl,
	): UserLocalDataSource

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
