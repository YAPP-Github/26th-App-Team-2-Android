package com.yapp.breake.presentation.main.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yapp.breake.core.designsystem.R
import com.yapp.breake.core.designsystem.component.HorizontalSpacer
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray850
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

internal enum class BrakeSnackbarType {
	SUCCESS,
	ERROR,
}

@Composable
internal fun BrakeSnackbar(
	snackbarData: BrakeSnackbarData,
	modifier: Modifier = Modifier,
) {
	val type = try {
		BrakeSnackbarType.valueOf(snackbarData.actionLabel ?: "SUCCESS")
	} catch (_: IllegalArgumentException) {
		BrakeSnackbarType.SUCCESS
	}

	val icon = when (type) {
		BrakeSnackbarType.SUCCESS -> R.drawable.ic_snackbar_success
		BrakeSnackbarType.ERROR -> R.drawable.ic_snackbar_error
	}

	SnackbarContent(
		icon = icon,
		message = snackbarData.message,
		modifier = modifier,
		onDismiss = snackbarData.onAction,
	)
}

@Composable
private fun SnackbarContent(
	icon: Int,
	message: String,
	modifier: Modifier = Modifier,
	onDismiss: (() -> Unit),
) {

	val offsetX = remember { Animatable(0f) }
	val coroutineScope = rememberCoroutineScope()

	val density = LocalDensity.current
	val swipeThreshold = with(density) { 160.dp.toPx() }

	var dragStartTime by remember { mutableLongStateOf(0L) }
	var dragStartX by remember { mutableFloatStateOf(0f) }

	Box(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 12.dp)
			.graphicsLayer {
				translationX = offsetX.value
				alpha = 1f - (abs(offsetX.value) / (swipeThreshold))
			}
			.pointerInput(Unit) {
				detectHorizontalDragGestures(
					onDragStart = { offset ->
						dragStartTime = System.currentTimeMillis()
						dragStartX = offset.x
					},
					onDragEnd = {
						coroutineScope.launch {
							val dragDuration = System.currentTimeMillis() - dragStartTime
							val dragDistance = abs(offsetX.value)
							val velocity = if (dragDuration > 0) {
								(dragDistance * 1000f) / dragDuration // pixels per second
							} else {
								0f
							}
							Timber.d("dismiss 속도: $velocity px/s")

							// 속도 기반 dismiss 분기 처리
							val dynamicThreshold = when {
								velocity > 5000f -> swipeThreshold * 0.3f // 매우 빠른 스와이프
								velocity > 2500f -> swipeThreshold * 0.5f // 빠른 스와이프
								velocity > 1000f -> swipeThreshold * 0.8f // 보통 속도
								else -> swipeThreshold // 느린 드래그
							}

							if (abs(offsetX.value) > dynamicThreshold) {
								onDismiss()
							} else {
								offsetX.animateTo(
									targetValue = 0f,
									animationSpec = tween(
										durationMillis = 300,
										easing = FastOutSlowInEasing,
									),
								)
							}
						}
					},
					onHorizontalDrag = { _, dragAmount ->
						coroutineScope.launch {
							offsetX.snapTo(offsetX.value + dragAmount)
						}
					},
				)
			}
			.background(
				color = Gray850,
				shape = RoundedCornerShape(16.dp),
			)
			.padding(16.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				painter = painterResource(id = icon),
				contentDescription = null,
				tint = Color.Unspecified,
			)
			HorizontalSpacer(10.dp)
			Text(
				text = message,
				style = BrakeTheme.typography.body14M,
				color = Color.White,
				modifier = Modifier.weight(1f),
			)
		}
	}
}

@Preview
@Composable
private fun BrakeSnackbarPreview() {
	BrakeTheme {
		SnackbarContent(
			icon = R.drawable.ic_snackbar_success,
			message = "This is a success message!",
			modifier = Modifier.fillMaxWidth(),
			onDismiss = {},
		)
	}
}
