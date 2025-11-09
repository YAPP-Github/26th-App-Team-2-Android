package com.teambrake.brake.data.remote.source.di

import com.teambrake.brake.data.remote.source.AccountRemoteDataSource
import com.teambrake.brake.data.remote.source.AccountRemoteDataSourceImpl
import com.teambrake.brake.data.remote.source.AppGroupRemoteDataSource
import com.teambrake.brake.data.remote.source.AppGroupRemoteDataSourceImpl
import com.teambrake.brake.data.remote.source.NameRemoteDataSource
import com.teambrake.brake.data.remote.source.NameRemoteDataSourceImpl
import com.teambrake.brake.data.remote.source.StatisticRemoteDataSource
import com.teambrake.brake.data.remote.source.StatisticRemoteDataSourceImpl
import com.teambrake.brake.data.remote.source.TokenRemoteDataSource
import com.teambrake.brake.data.remote.source.TokenRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class RemoteSourceModule {

	@Binds
	@Singleton
	abstract fun bindTokenRemoteDataSource(
		tokenRemoteDataSource: TokenRemoteDataSourceImpl,
	): TokenRemoteDataSource

	@Binds
	@Singleton
	abstract fun bindNicknameRemoteDataSource(
		nameRemoteDataSourceImpl: NameRemoteDataSourceImpl,
	): NameRemoteDataSource

	@Binds
	@Singleton
	abstract fun bindAccountRemoteDataSource(
		accountRemoteDataSource: AccountRemoteDataSourceImpl,
	): AccountRemoteDataSource

	@Binds
	@Singleton
	abstract fun bindAppGroupRemoteDataSource(
		appGroupRemoteDataSource: AppGroupRemoteDataSourceImpl,
	): AppGroupRemoteDataSource

	@Binds
	@Singleton
	abstract fun bindSessionRemoteDataSource(
		sessionRemoteDataSource: StatisticRemoteDataSourceImpl,
	): StatisticRemoteDataSource
}
