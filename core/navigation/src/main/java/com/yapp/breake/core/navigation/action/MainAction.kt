package com.yapp.breake.core.navigation.action

import androidx.compose.runtime.Composable

interface MainAction {
	@Composable fun OnFinishBackHandler()

	@Composable fun OnShowLogoutDialog(onConfirm: () -> Unit, onDismiss: () -> Unit)

	@Composable fun OnShowLoading()
	fun onShowErrorMessage(message: String)
	fun onShowSuccessMessage(message: String)
}
