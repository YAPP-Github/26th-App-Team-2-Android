package com.yapp.breake.core.navigation.action

import androidx.compose.runtime.Composable

interface MainAction {
	fun onFinish()

	@Composable fun OnShowLoading()
	fun onShowErrorMessage(message: String)
	fun onShowSuccessMessage(message: String)
}
