package com.yapp.breake.presentation.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SignupRoute(
	navigateToLogin: () -> Unit,
	onShowErrorSnackBar: (Throwable?) -> Unit,
	// viewModel: SignupViewModel = hiltViewModel(),
) {
	SignupScreen()
}

@Composable
fun SignupScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(text = "회원가입")
	}
}
