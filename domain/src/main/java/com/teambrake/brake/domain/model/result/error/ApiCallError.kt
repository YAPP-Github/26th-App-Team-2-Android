package com.teambrake.brake.domain.model.result.error

/** UseCase error **/
sealed interface DecideStartDestinationUseCaseError
sealed interface StartOfflineModeUseCaseError
sealed interface DeleteAccountUseCaseError
sealed interface LogoutUseCaseError
sealed interface DecideNextDestinationFromPermissionUseCaseError

/** API Call error **/
sealed interface ApiCallError :
	DecideStartDestinationUseCaseError,
	StartOfflineModeUseCaseError,
	DeleteAccountUseCaseError,
	LogoutUseCaseError,
	DecideNextDestinationFromPermissionUseCaseError
