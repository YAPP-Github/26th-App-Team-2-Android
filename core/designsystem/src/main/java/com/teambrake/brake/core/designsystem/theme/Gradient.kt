package com.teambrake.brake.core.designsystem.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

val LinerGradient = Brush.linearGradient(
	colors = listOf(
		Color(0x26C0DBFF),
		Color(0x0BC0DBFF),
		Color(0x00C0DBFF),
	),
	start = Offset(0f, 0f),
	end = Offset(0f, Float.POSITIVE_INFINITY),
)

val AppItemGradient = Brush.linearGradient(
	colorStops = arrayOf(
		0.2837f to Color(0xFF292C31),
		0.7582f to Color(0xFF32363B),
	),
	start = Offset(0f, 0f),
	end = Offset(
		cos(110.23 * PI / 180).toFloat() * 1000,
		sin(110.23 * PI / 180).toFloat() * 1000,
	),
)
