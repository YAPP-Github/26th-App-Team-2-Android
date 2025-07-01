package com.yapp.breake.core.designsystem.component

import androidx.annotation.FloatRange
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

@Composable
fun VerticalSpacer(
	height: Dp
) {
	Spacer(modifier = Modifier.height(height))
}

@Composable
fun HorizontalSpacer(
	width: Dp
) {
	Spacer(modifier = Modifier.width(width))
}

@Composable
fun RowScope.HorizontalSpacer(
	@FloatRange(from = 0.0, fromInclusive = false)
	width: Float
) {
	Spacer(modifier = Modifier.weight(width))
}

@Composable
fun ColumnScope.VerticalSpacer(
	@FloatRange(from = 0.0, fromInclusive = false)
	height: Float
) {
	Spacer(modifier = Modifier.weight(height))
}
