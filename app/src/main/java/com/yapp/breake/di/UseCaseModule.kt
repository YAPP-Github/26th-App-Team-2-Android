package com.yapp.breake.di

import com.yapp.breake.domain.usecase.CheckAuthCodeUseCase
import com.yapp.breake.domain.usecase.CheckAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCaseImpl
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCase
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCaseImpl
import com.yapp.breake.domain.usecase.DeleteAccountUseCase
import com.yapp.breake.domain.usecase.DeleteAccountUseCaseImpl
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.domain.usecase.LoginUseCaseImpl
import com.yapp.breake.domain.usecase.LoginWithCachedAuthCodeUseCase
import com.yapp.breake.domain.usecase.LoginWithCachedAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecase.LogoutUseCase
import com.yapp.breake.domain.usecase.LogoutUseCaseImpl
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCaseImpl
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

	@Binds
	abstract fun bindStoreOnboardingCompletionUseCase(
		storeOnboardingCompletionUseCase: StoreOnboardingCompletionUseCaseImpl,
	): StoreOnboardingCompletionUseCase

	@Binds
	abstract fun bindDecideStartDestinationUseCase(
		decideStartDestinationUseCase: DecideStartDestinationUseCaseImpl,
	): DecideStartDestinationUseCase

	@Binds
	abstract fun bindDecideNextDestinationFromPermissionUseCase(
		decideNextDestinationFromPermissionUseCase: DecideNextDestinationFromPermissionUseCaseImpl,
	): DecideNextDestinationFromPermissionUseCase

	@Binds
	abstract fun bindLogoutUseCase(
		logoutUseCase: LogoutUseCaseImpl,
	): LogoutUseCase

	@Binds
	abstract fun bindDeleteAccountUseCase(
		deleteAccountUseCase: DeleteAccountUseCaseImpl,
	): DeleteAccountUseCase
}
