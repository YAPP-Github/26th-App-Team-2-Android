package com.teambrake.brake.domain.model.result.error

data class UndefinedExceptionError(val exception: Throwable) : ApiCallError
