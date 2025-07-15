package com.yapp.breake.data.repository.di

import com.yapp.breake.data.repository.LocalInfoRepositoryImpl
import com.yapp.breake.data.repository.RemoteLoginRepositoryImpl
import com.yapp.breake.data.repository.RemoteNameRepositoryImpl
import com.yapp.breake.data.repository.LocalTokenRepositoryImpl
import com.yapp.breake.domain.repository.LocalInfoRepository
import com.yapp.breake.domain.repository.RemoteLoginRepository
import com.yapp.breake.domain.repository.RemoteNameRepository
import com.yapp.breake.domain.repository.LocalTokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

	@Binds
	@Named("RemoteLoginRepo")
	abstract fun bindRemoteLoginRepository(
		remoteLoginRepository: RemoteLoginRepositoryImpl,
	): RemoteLoginRepository

	@Binds
	@Singleton
	abstract fun bindLocalTokenRepository(
		localTokenRepository: LocalTokenRepositoryImpl,
	): LocalTokenRepository

	@Binds
	@Singleton
	@Named("RemoteNameRepo")
	abstract fun bindRemoteNameRepository(
		remoteNameRepository: RemoteNameRepositoryImpl,
	): RemoteNameRepository

	@Binds
	@Singleton
	abstract fun bindLocalInfoRepository(
		localInfoRepository: LocalInfoRepositoryImpl,
	): LocalInfoRepository
}
