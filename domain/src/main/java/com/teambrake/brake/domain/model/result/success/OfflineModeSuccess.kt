package com.teambrake.brake.domain.model.result.success

data class OfflineModeSuccess<out T>(val data: T) : ModeSuccess<T>
