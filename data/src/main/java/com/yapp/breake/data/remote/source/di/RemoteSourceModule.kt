package com.yapp.breake.data.remote.source.di

import com.yapp.breake.data.remote.source.AccountRemoteDataSource
import com.yapp.breake.data.remote.source.AccountRemoteDataSourceImpl
import com.yapp.breake.data.remote.source.AppGroupRemoteDataSource
import com.yapp.breake.data.remote.source.AppGroupRemoteDataSourceImpl
import com.yapp.breake.data.remote.source.NameRemoteDataSource
import com.yapp.breake.data.remote.source.NameRemoteDataSourceImpl
import com.yapp.breake.data.remote.source.TokenRemoteDataSource
import com.yapp.breake.data.remote.source.TokenRemoteDataSourceImpl
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
}
