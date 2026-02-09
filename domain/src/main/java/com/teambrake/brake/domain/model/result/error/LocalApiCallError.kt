package com.teambrake.brake.domain.model.result.error

data class LocalApiCallError(val e: Throwable) : ApiCallError
