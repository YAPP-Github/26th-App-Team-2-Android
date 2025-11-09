package com.teambrake.brake.data.test.repository.di

import com.teambrake.brake.data.test.repository.FakeTokenRepositoryImpl
import com.teambrake.brake.data.test.repository.FakeNicknameRepositoryImpl
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.repository.NicknameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
internal abstract class TestRepositoryModule {

	@Binds
	@Named("FakeTokenRepo")
	abstract fun bindFakeTokenRepository(
		fakeTokenRepository: FakeTokenRepositoryImpl,
	): TokenRepository

	@Binds
	@Named("FakeNicknameRepo")
	abstract fun bindFakeNicknameRepository(
		fakeNicknameRepository: FakeNicknameRepositoryImpl,
	): NicknameRepository
}
