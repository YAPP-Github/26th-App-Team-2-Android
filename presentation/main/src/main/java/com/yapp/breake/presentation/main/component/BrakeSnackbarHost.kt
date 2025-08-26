package com.yapp.breake.presentation.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

internal data class BrakeSnackbarData(
	val message: String,
	val actionLabel: String?,
	val duration: Long,
	val onAction: (() -> Unit) = {},
)

@Stable
internal class BrakeSnackbarHostState {
	private val _currentSnackbarData = mutableStateOf<BrakeSnackbarData?>(null)
	val currentSnackbarData: State<BrakeSnackbarData?> = _currentSnackbarData
	private var currentJob: Job? = null

	fun showSnackbar(
		message: String,
		actionLabel: String?,
		duration: Long = 5000000L,
		onAction: () -> Unit,
	) {
		// 이전 스낵바가 표시되고 있다면 취소
		currentJob?.run {
			if (isActive) {
				_currentSnackbarData.value = null
				cancel()
			}
		}

		currentJob = CoroutineScope(Dispatchers.IO).launch {

			// 약간의 딜레이로 애니메이션 자연스럽게 처리
			delay(100)

			val snackbarData = BrakeSnackbarData(
				message = message,
				actionLabel = actionLabel,
				duration = duration,
				onAction = onAction,
			)

			_currentSnackbarData.value = snackbarData

			delay(duration)
			if (isActive) {
				_currentSnackbarData.value = null
			}
		}
	}

	fun dismiss() {
		currentJob?.cancel()
		_currentSnackbarData.value = null
	}
}

@Composable
internal fun BrakeSnackbarHost(
	hostState: BrakeSnackbarHostState,
	snackbar: @Composable (BrakeSnackbarData) -> Unit,
	modifier: Modifier = Modifier,
) {
	val currentSnackbarData by hostState.currentSnackbarData

	AnimatedVisibility(
		visible = currentSnackbarData != null,
		enter = slideInVertically(
			initialOffsetY = { it },
			animationSpec = tween(
				durationMillis = 300,
				easing = FastOutSlowInEasing,
			),
		) + fadeIn(
			animationSpec = tween(
				durationMillis = 800,
				easing = FastOutSlowInEasing,
			),
		),
		modifier = modifier,
	) {
		currentSnackbarData?.let { data ->
			snackbar(data)
		}
	}
}
