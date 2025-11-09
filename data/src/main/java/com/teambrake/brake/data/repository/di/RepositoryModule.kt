package com.teambrake.brake.data.repository.di

import com.teambrake.brake.data.repository.AppGroupRepositoryImpl
import com.teambrake.brake.data.repository.SessionRepositoryImpl
import com.teambrake.brake.data.repository.StatisticRepositoryImpl
import com.teambrake.brake.data.repository.TokenRepositoryImpl
import com.teambrake.brake.data.repository.NicknameRepositoryImpl
import com.teambrake.brake.data.repositoryImpl.AppRepositoryImpl
import com.teambrake.brake.domain.repository.AppGroupRepository
import com.teambrake.brake.domain.repository.AppRepository
import com.teambrake.brake.domain.repository.SessionRepository
import com.teambrake.brake.domain.repository.StatisticRepository
import com.teambrake.brake.domain.repository.TokenRepository
import com.teambrake.brake.domain.repository.NicknameRepository
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

	@Binds
	@Singleton
	abstract fun bindAppGroupRepository(
		appGroupRepositoryImpl: AppGroupRepositoryImpl,
	): AppGroupRepository

	@Binds
	@Singleton
	abstract fun bindAppRepository(
		appRepositoryImpl: AppRepositoryImpl,
	): AppRepository

	@Binds
	@Singleton
	abstract fun bindStatisticRepository(
		statisticRepositoryImpl: StatisticRepositoryImpl,
	): StatisticRepository
}
