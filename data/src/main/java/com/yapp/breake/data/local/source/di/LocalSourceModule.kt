package com.yapp.breake.data.local.source.di

import com.yapp.breake.data.local.source.AuthLocalDataSource
import com.yapp.breake.data.local.source.AuthLocalDataSourceImpl
import com.yapp.breake.data.local.source.TokenLocalDataSource
import com.yapp.breake.data.local.source.TokenLocalDataSourceImpl
import com.yapp.breake.data.local.source.UserLocalDataSource
import com.yapp.breake.data.local.source.UserLocalDataSourceImpl
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
}
