package com.yapp.breake.data.test.repository.di

import com.yapp.breake.data.test.repository.FakeLoginRepositoryImpl
import com.yapp.breake.domain.repository.LoginRepository
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
		fakeLoginRepository: FakeLoginRepositoryImpl,
	): LoginRepository
}
