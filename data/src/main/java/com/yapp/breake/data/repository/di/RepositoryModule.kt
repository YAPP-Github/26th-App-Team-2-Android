package com.yapp.breake.data.repository.di

import com.yapp.breake.data.repository.SessionRepositoryImpl
import com.yapp.breake.data.repository.TokenRepositoryImpl
import com.yapp.breake.data.repository.NicknameRepositoryImpl
import com.yapp.breake.domain.repository.SessionRepository
import com.yapp.breake.domain.repository.TokenRepository
import com.yapp.breake.domain.repository.NicknameRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

	@Binds
	@Named("TokenRepo")
	abstract fun bindTokenRepository(
		remoteTokenRepository: TokenRepositoryImpl,
	): TokenRepository

	@Binds
	@Named("NicknameRepo")
	abstract fun bindNicknameRepository(
		nicknameRepository: NicknameRepositoryImpl,
	): NicknameRepository

	@Binds
	abstract fun bindSessionRepository(
		sessionRepository: SessionRepositoryImpl,
	): SessionRepository
}
