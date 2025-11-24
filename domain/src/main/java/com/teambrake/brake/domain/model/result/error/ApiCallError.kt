package com.teambrake.brake.domain.model.result.error

/** UseCase error **/
sealed interface DecideStartDestinationUseCaseError

/** API Call error **/
sealed interface ApiCallError : DecideStartDestinationUseCaseError
