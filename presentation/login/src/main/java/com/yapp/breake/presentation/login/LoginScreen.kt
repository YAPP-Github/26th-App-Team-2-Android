package com.yapp.breake.presentation.login

import androidx.compose.runtime.Composable

@Composable
fun LoginRoute(
	navigateToSignup: () -> Unit,
	navigateToOnboarding: () -> Unit,
	navigateToHome: () -> Unit,
	onShowErrorSnackBar: (Throwable?) -> Unit
	// viewModel: LoginViewModel = hiltViewModel(),
) {
	// LoginScreen()
}
