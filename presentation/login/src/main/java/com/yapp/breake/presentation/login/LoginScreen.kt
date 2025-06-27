package com.yapp.breake.presentation.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoginRoute(
	navigateToSignup: () -> Unit,
	navigateToOnboarding: () -> Unit,
	navigateToHome: () -> Unit,
	onShowErrorSnackBar: (Throwable?) -> Unit
	// viewModel: LoginViewModel = hiltViewModel(),
) {
	LoginScreen()
}

@Composable
fun LoginScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(text = "로그인")
	}
}
