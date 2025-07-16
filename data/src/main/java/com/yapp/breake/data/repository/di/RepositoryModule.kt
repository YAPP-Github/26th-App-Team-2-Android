package com.yapp.breake.data.repository.di

import com.yapp.breake.data.repository.LoginRepositoryImpl
import com.yapp.breake.data.repository.NicknameRepositoryImpl
import com.yapp.breake.domain.repository.LoginRepository
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
	@Named("LoginRepo")
	abstract fun bindLoginRepository(
		remoteLoginRepository: LoginRepositoryImpl,
	): LoginRepository

	@Binds
	@Named("NicknameRepo")
	abstract fun bindNicknameRepository(
		nicknameRepository: NicknameRepositoryImpl,
	): NicknameRepository
}
