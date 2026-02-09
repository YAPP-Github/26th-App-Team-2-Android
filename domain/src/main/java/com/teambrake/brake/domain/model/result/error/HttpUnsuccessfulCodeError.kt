package com.teambrake.brake.domain.model.result.error

data class HttpUnsuccessfulCodeError(val httpCode: Int) : ApiCallError
