package com.yapp.breake.core.navigation.action

interface MainAction {
	fun onFinish()
	fun onShowErrorMessage(message: String)
	fun onShowSuccessMessage(message: String)
}
