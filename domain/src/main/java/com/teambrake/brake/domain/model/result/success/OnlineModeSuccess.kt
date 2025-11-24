package com.teambrake.brake.domain.model.result.success

data class OnlineModeSuccess<out T>(val data: T) : ModeSuccess<T>
