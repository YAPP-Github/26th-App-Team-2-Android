package com.yapp.breake.data.usecase.di

import com.yapp.breake.domain.usecase.CheckAuthCodeUseCase
import com.yapp.breake.domain.usecase.CheckAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.domain.usecase.LoginUseCaseImpl
import com.yapp.breake.domain.usecase.LoginWithCachedAuthCodeUseCase
import com.yapp.breake.domain.usecase.LoginWithCachedAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.domain.usecase.UpdateNicknameUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class UseCaseModule {

	@Binds
	abstract fun bindLoginUseCase(
		loginUseCase: LoginUseCaseImpl,
	): LoginUseCase

	@Binds
	abstract fun bindCheckAuthCodeUseCase(
		checkAuthCodeUseCase: CheckAuthCodeUseCaseImpl,
	): CheckAuthCodeUseCase

	@Binds
	abstract fun bindLoginWithCachedAuthCodeUseCase(
		loginWithCachedAuthCodeUseCase: LoginWithCachedAuthCodeUseCaseImpl,
	): LoginWithCachedAuthCodeUseCase

	@Binds
	abstract fun bindUpdateNicknameUseCase(
		updateNicknameUseCase: UpdateNicknameUseCaseImpl,
	): UpdateNicknameUseCase
}
