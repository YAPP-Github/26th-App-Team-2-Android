package com.yapp.breake.data.repository.di

import com.yapp.breake.data.repository.LoginRepositoryImpl
import com.yapp.breake.domain.repository.LoginRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal abstract class RepositoryModule {

	@Binds
	@Singleton
	abstract fun bindLoginRepository(
		loginRepository: LoginRepositoryImpl,
	): LoginRepository
}
