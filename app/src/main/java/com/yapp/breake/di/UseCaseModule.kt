package com.yapp.breake.di

import com.yapp.breake.domain.usecase.CheckAuthCodeUseCase
import com.yapp.breake.domain.usecase.CreateNewGroupUseCase
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.yapp.breake.domain.usecase.DecideNextDestinationFromPermissionUseCaseImpl
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCase
import com.yapp.breake.domain.usecase.DecideStartDestinationUseCaseImpl
import com.yapp.breake.domain.usecase.DeleteAccountUseCase
import com.yapp.breake.domain.usecase.DeleteAccountUseCaseImpl
import com.yapp.breake.domain.usecase.DeleteGroupUseCase
import com.yapp.breake.domain.usecase.FindAppGroupUseCase
import com.yapp.breake.domain.usecase.GetNicknameUseCase
import com.yapp.breake.domain.usecase.LoginUseCase
import com.yapp.breake.domain.usecase.LoginWithCachedAuthCodeUseCase
import com.yapp.breake.domain.usecase.LogoutUseCase
import com.yapp.breake.domain.usecase.LogoutUseCaseImpl
import com.yapp.breake.domain.usecase.ResetAppGroupUsecase
import com.yapp.breake.domain.usecase.ResetAppGroupUsecaseImpl
import com.yapp.breake.domain.usecase.SetAlarmUseCase
import com.yapp.breake.domain.usecase.SetBlockingAlarmUseCase
import com.yapp.breake.domain.usecase.SetSnoozeAlarmUseCase
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCase
import com.yapp.breake.domain.usecase.StoreOnboardingCompletionUseCaseImpl
import com.yapp.breake.domain.usecase.UpdateNicknameUseCase
import com.yapp.breake.domain.usecaseImpl.CheckAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.CreateNewGroupUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.DeleteGroupUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.FindAppGroupUsecaseImpl
import com.yapp.breake.domain.usecaseImpl.GetNicknameUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.LoginUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.LoginWithCachedAuthCodeUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.SetAlarmUsecaseImpl
import com.yapp.breake.domain.usecaseImpl.SetBlockingAlarmUseCaseImpl
import com.yapp.breake.domain.usecaseImpl.SetSnoozeAlarmUsecaseImpl
import com.yapp.breake.domain.usecaseImpl.UpdateNicknameUseCaseImpl
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
	abstract fun bindGetNicknameUseCase(
		getNicknameUseCase: GetNicknameUseCaseImpl,
	): GetNicknameUseCase

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

	@Binds
	abstract fun bindFindAppGroupUseCase(
		findAppGroupUsecase: FindAppGroupUsecaseImpl,
	): FindAppGroupUseCase

	@Binds
	abstract fun bindSetAlarmUseCase(
		setAlarmUsecase: SetAlarmUsecaseImpl,
	): SetAlarmUseCase

	@Binds
	abstract fun bindSetSnoozeAlarmUseCase(
		setSnoozeAlarmUsecase: SetSnoozeAlarmUsecaseImpl,
	): SetSnoozeAlarmUseCase

	@Binds
	abstract fun bindSetBlockingAlarmUseCase(
		setBlockingAlarmUsecase: SetBlockingAlarmUseCaseImpl,
	): SetBlockingAlarmUseCase

	@Binds
	abstract fun bindResetAppGroupUsecase(
		resetAppGroupUsecase: ResetAppGroupUsecaseImpl,
	): ResetAppGroupUsecase

	@Binds
	abstract fun bindCreateNewGroupUseCase(
		createNewGroupUseCase: CreateNewGroupUseCaseImpl,
	): CreateNewGroupUseCase

	@Binds
	abstract fun bindDeleteGroupUseCase(
		deleteGroupUseCase: DeleteGroupUseCaseImpl,
	): DeleteGroupUseCase
}
