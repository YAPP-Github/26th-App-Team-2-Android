package com.teambrake.brake.domain.model.result

sealed class BrakeResult<out S, out E> {

	data class Success<out S>(val data: S) : BrakeResult<S, Nothing>()

	data class Error<out E>(val error: E) : BrakeResult<Nothing, E>()
}
