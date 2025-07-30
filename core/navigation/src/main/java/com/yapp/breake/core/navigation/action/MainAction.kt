package com.yapp.breake.core.navigation.action

interface MainAction {
	fun onFinish()
	fun onShowSnackBar(throwable: Throwable?)
	fun onShowErrorMessage(message: String)
	fun onShowSuccessMessage(message: String)
}
