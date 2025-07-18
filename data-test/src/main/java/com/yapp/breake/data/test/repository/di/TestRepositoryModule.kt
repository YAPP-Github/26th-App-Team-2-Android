package com.yapp.breake.data.test.repository.di

import com.yapp.breake.data.test.repository.FakeTokenRepositoryImpl
import com.yapp.breake.data.test.repository.FakeNicknameRepositoryImpl
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.repository.NicknameRepository
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
