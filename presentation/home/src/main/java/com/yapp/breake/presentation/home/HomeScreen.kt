package com.yapp.breake.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun HomeRoute(
	padding: PaddingValues,
	onShowErrorSnackBar: (throwable: Throwable?) -> Unit
	// viewModel: HomeViewModel = hiltViewModel(),
) {
	HomeScreen()
}

@Composable
fun HomeScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(text = "관리")
	}
}
