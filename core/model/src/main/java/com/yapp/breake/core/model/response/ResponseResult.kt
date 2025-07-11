package com.yapp.breake.core.model.response

sealed interface ResponseResult<out T> {
	data class Success<T>(val data: T) : ResponseResult<T>
	data class Error(val message: String) : ResponseResult<Nothing>
	data class Exception(val exception: Throwable) : ResponseResult<Nothing>
}
