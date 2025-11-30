package com.teambrake.brake.domain.model.result.error

/** UseCase error **/
sealed interface DecideStartDestinationUseCaseError
sealed interface StartOfflineModeUseCaseError

/** API Call error **/
sealed interface ApiCallError :
	DecideStartDestinationUseCaseError,
	StartOfflineModeUseCaseError,
