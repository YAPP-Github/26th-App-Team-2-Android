package com.yapp.breake.core.designsystem.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val LinerGradient = Brush.linearGradient(
	colors = listOf(
		Color(0x26C0DBFF),
		Color(0x0BC0DBFF),
		Color(0x00C0DBFF),
	),
	start = Offset(0f, 0f),
	end = Offset(0f, Float.POSITIVE_INFINITY),
)
