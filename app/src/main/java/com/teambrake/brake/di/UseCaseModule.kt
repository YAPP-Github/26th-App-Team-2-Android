package com.teambrake.brake.di

import com.teambrake.brake.domain.usecase.CreateNewGroupUseCase
import com.teambrake.brake.domain.usecase.DecideNextDestinationFromPermissionUseCase
import com.teambrake.brake.domain.usecase.DecideStartDestinationUseCase
import com.teambrake.brake.domain.usecase.DeleteAccountUseCase
import com.teambrake.brake.domain.usecase.DeleteGroupUseCase
import com.teambrake.brake.domain.usecase.FindAppGroupUseCase
import com.teambrake.brake.domain.usecase.GetNicknameUseCase
import com.teambrake.brake.domain.usecase.GrantNewGroupIdUseCase
import com.teambrake.brake.domain.usecase.LoginUseCase
import com.teambrake.brake.domain.usecase.LogoutUseCase
import com.teambrake.brake.domain.usecase.ResetAppGroupUsecase
import com.teambrake.brake.domain.usecase.SetAlarmUseCase
import com.teambrake.brake.domain.usecase.SetBlockingAlarmUseCase
import com.teambrake.brake.domain.usecase.SetSnoozeAlarmUseCase
import com.teambrake.brake.domain.usecase.StartOfflineModeUseCase
import com.teambrake.brake.domain.usecase.StoreOnboardingCompletionUseCase
import com.teambrake.brake.domain.usecase.UpdateNicknameUseCase
import com.teambrake.brake.domain.usecaseImpl.CreateNewGroupUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.DecideNextDestinationFromPermissionUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.DecideStartDestinationUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.DeleteAccountUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.DeleteGroupUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.FindAppGroupUsecaseImpl
import com.teambrake.brake.domain.usecaseImpl.GetNicknameUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.GrantNewGroupIdUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.LoginUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.LogoutUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.ResetAppGroupUsecaseImpl
import com.teambrake.brake.domain.usecaseImpl.SetAlarmUsecaseImpl
import com.teambrake.brake.domain.usecaseImpl.SetBlockingAlarmUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.SetSnoozeAlarmUsecaseImpl
import com.teambrake.brake.domain.usecaseImpl.StartOfflineModeUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.StoreOnboardingCompletionUseCaseImpl
import com.teambrake.brake.domain.usecaseImpl.UpdateNicknameUseCaseImpl
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
	abstract fun bindStartOfflineModeUseCase(
		startOfflineModeUseCase: StartOfflineModeUseCaseImpl,
	): StartOfflineModeUseCase

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

	@Binds
	abstract fun bindGrantNewGroupIdUseCase(
		grantNewGroupIdUseCase: GrantNewGroupIdUseCaseImpl,
	): GrantNewGroupIdUseCase
}
