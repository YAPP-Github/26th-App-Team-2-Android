package com.yapp.breake.core.navigation.action

interface MainAction {
	fun onFinish()
	fun onShowSnackBar(throwable: Throwable?)
	fun onShowErrorSnackBar(message: String)
	fun onShowMessage(message: String)
}
