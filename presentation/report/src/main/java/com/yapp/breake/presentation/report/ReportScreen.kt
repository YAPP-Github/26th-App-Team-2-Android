package com.yapp.breake.presentation.report

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ReportRoute(padding: PaddingValues) {
	ReportScreen()
}

@Composable
fun ReportScreen() {
	Box(
		modifier = Modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(text = "리포트")
	}
}
