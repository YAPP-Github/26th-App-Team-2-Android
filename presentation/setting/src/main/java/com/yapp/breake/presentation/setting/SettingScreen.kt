package com.yapp.breake.presentation.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SettingRoute(
	padding: PaddingValues,
	onShowErrorSnackBar: (Throwable?) -> Unit = {},
	onChangeDarkTheme: (Boolean) -> Unit
) {
	SettingScreen()
}

@Composable
fun SettingScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(text = "내 정보")
	}
}
