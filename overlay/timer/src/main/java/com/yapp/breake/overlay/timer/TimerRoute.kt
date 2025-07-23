package com.yapp.breake.overlay.timer

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.overlay.timer.component.InitScreen
import com.yapp.breake.overlay.timer.component.SetCompleteScreen
import com.yapp.breake.overlay.timer.component.TimerScreen

@Composable
fun TimerRoute(
	appName: String,
	groupId: Long,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
) {
	TimerOverlay(
		appName = appName,
		groupId = groupId,
		onExitManageApp = onExitManageApp,
		onCloseOverlay = onCloseOverlay,
	)
}

@Composable
private fun TimerOverlay(
	appName: String,
	groupId: Long,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
	viewModel: TimerViewModel = hiltViewModel(),
) {
	val context = LocalContext.current
	val timerUiState by viewModel.timerUiState.collectAsStateWithLifecycle()
	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		TimerContent(
			appName = appName,
			onSetTime = viewModel::setTime,
			onConfirm = viewModel::setTime,
			onCloseOverlay = onCloseOverlay,
			onExitManageApp = onExitManageApp,
			onTimerConfirm = {
				viewModel.setBreakTimeAlarm(groupId)
			},
			timerUiState = timerUiState,
		)
	}

	LaunchedEffect(Unit) {
		viewModel.toastEffect.collect { message ->
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		}
	}
}

@Composable
private fun TimerContent(
	appName: String,
	onSetTime: (Int) -> Unit,
	onTimerConfirm: () -> Unit,
	onConfirm: () -> Unit,
	onCloseOverlay: () -> Unit,
	onExitManageApp: () -> Unit,
	timerUiState: TimerUiState,
) {
	AnimatedContent(
		targetState = timerUiState,
		transitionSpec = {
			fadeIn(animationSpec = tween(400)) togetherWith fadeOut(animationSpec = tween(400))
		},
	) { state ->
		when (state) {
			TimerUiState.Init -> {
				InitScreen(
					appName = appName,
					onConfirm = onConfirm,
					onExitManageApp = onExitManageApp,
				)
			}

			is TimerUiState.TimeSetting -> {
				TimerScreen(
					appName = appName,
					onTimeChange = onSetTime,
					onComplete = onTimerConfirm,
				)
			}

			is TimerUiState.SetComplete -> {
				SetCompleteScreen(
					durationMinutes = state.durationMinutes,
					endTime = state.endTime,
					onCloseOverlay = onCloseOverlay,
				)
			}
		}
	}
}
