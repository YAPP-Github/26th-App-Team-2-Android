package com.teambrake.brake.presentation.home.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.designsystem.theme.Gray600

@Composable
internal fun CircularProgressTimer(
	progress: Float,
	modifier: Modifier = Modifier,
	startColor: Color = Color(0xFFB6C1E0),
	endColor: Color = Color(0xFFF2FF5E),
	strokeWidth: Dp = 8.dp,
	backgroundColor: Color = Gray600,
	content: @Composable () -> Unit = {},
) {
	val animatedProgress by animateFloatAsState(
		targetValue = progress,
		animationSpec = tween(durationMillis = 150),
		label = "progress_animation",
	)

	Box(
		modifier = modifier
			.fillMaxWidth()
			.aspectRatio(1f)
			.padding(horizontal = 16.dp),
		contentAlignment = Alignment.Center,
	) {
		Canvas(
			modifier = Modifier
				.fillMaxWidth()
				.aspectRatio(1f),
		) {
			val strokeWidthPx = strokeWidth.toPx()
			val size = Size(this.size.width - strokeWidthPx, this.size.height - strokeWidthPx)
			val topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
			val center = Offset(this.size.width / 2, this.size.height / 2)
			val radius = (this.size.minDimension - strokeWidthPx) / 2

			drawCircle(
				color = backgroundColor.copy(alpha = 0.1f),
				radius = radius,
				center = center,
				style = Stroke(
					width = strokeWidthPx,
					cap = StrokeCap.Round,
				),
			)

			if (animatedProgress > 0f) {
				val progressBrush = Brush.linearGradient(
					colors = listOf(
						startColor,
						endColor,
					),
					start = Offset(center.x, 0f),
					end = Offset(
						center.x + radius * kotlin.math.cos(2 * kotlin.math.PI * animatedProgress - kotlin.math.PI / 2)
							.toFloat(),
						center.y + radius * kotlin.math.sin(2 * kotlin.math.PI * animatedProgress - kotlin.math.PI / 2)
							.toFloat(),
					),
				)

				drawArc(
					brush = progressBrush,
					startAngle = -90f,
					sweepAngle = 360f * animatedProgress,
					useCenter = false,
					topLeft = topLeft,
					size = size,
					style = Stroke(
						width = strokeWidthPx,
						cap = StrokeCap.Round,
					),
				)
			}
		}

		content()
	}
}

@Preview
@Composable
private fun CircularProgressTimerPreview() {
	BrakeTheme {
		CircularProgressTimer(
			progress = 0.7f,
		)
	}
}
