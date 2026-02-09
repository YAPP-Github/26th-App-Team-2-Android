package com.teambrake.brake.domain.model.result.success

data class PreAuthSuccess<out T>(val data: T) : AuthStatusSuccess<T>
