package com.yapp.breake.core.designsystem.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.theme.BrakeYellow

@Composable
fun DotProgressIndicator(
	dotCount: Int = 4,
	dotSize: Dp = 12.dp,
	dotColor: Color = BrakeYellow,
	animationDuration: Int = 600,
	staggerDelay: Int = 150,
) {
	val transition = rememberInfiniteTransition()
	Row(
		horizontalArrangement = Arrangement.spacedBy(dotSize),
	) {
		repeat(dotCount) { index ->
			// Scale 애니메이션
			val scale by transition.animateFloat(
				initialValue = 0.4f,
				targetValue = 1.0f,
				animationSpec = infiniteRepeatable(
					animation = tween(
						durationMillis = animationDuration,
						easing = FastOutLinearInEasing,
					),
					repeatMode = RepeatMode.Reverse,
					initialStartOffset = StartOffset(index * staggerDelay),
				),
			)
			// Alpha 애니메이션
			val alpha by transition.animateFloat(
				initialValue = 0.3f,
				targetValue = 1f,
				animationSpec = infiniteRepeatable(
					animation = tween(
						durationMillis = animationDuration,
						easing = FastOutLinearInEasing,
					),
					repeatMode = RepeatMode.Reverse,
					initialStartOffset = StartOffset(index * staggerDelay),
				),
			)
			Box(
				modifier = Modifier
					.size(dotSize)
					.graphicsLayer(
						scaleX = scale,
						scaleY = scale,
						alpha = alpha,
					)
					.background(color = dotColor, shape = CircleShape),
			)
		}
	}
}
