package com.yapp.breake.data.test.repository.di

import com.yapp.breake.data.test.repository.FakeRemoteLoginRepositoryImpl
import com.yapp.breake.data.test.repository.FakeRemoteNameRepositoryImpl
import com.yapp.breake.domain.repository.RemoteLoginRepository
import com.yapp.breake.domain.repository.RemoteNameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TestRepositoryModule {

	@Binds
	@Named("FakeLoginRepo")
	abstract fun bindFakeLoginRepository(
		fakeLoginRepository: FakeRemoteLoginRepositoryImpl,
	): RemoteLoginRepository

	@Binds
	@Named("FakeUserProfileRepo")
	abstract fun bindFakeUserProfileRepository(
		fakeUserProfileRepository: FakeRemoteNameRepositoryImpl,
	): RemoteNameRepository
}
