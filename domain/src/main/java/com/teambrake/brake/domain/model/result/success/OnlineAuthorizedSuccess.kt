package com.teambrake.brake.domain.model.result.success

data class OnlineAuthorizedSuccess<out T>(val data: T) : AuthStatusSuccess<T>
