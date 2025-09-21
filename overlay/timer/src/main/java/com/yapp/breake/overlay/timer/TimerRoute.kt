package com.yapp.breake.overlay.timer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yapp.breake.overlay.timer.component.InitScreen
import com.yapp.breake.overlay.timer.component.SetCompleteScreen

@Composable
fun TimerRoute(
	appName: String,
	groupName: String,
	groupId: Long,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
) {
	TimerOverlay(
		appName = appName,
		groupName = groupName,
		groupId = groupId,
		onExitManageApp = onExitManageApp,
		onCloseOverlay = onCloseOverlay,
	)
}

@Composable
private fun TimerOverlay(
	appName: String,
	groupName: String,
	groupId: Long,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
	viewModel: TimerViewModel = hiltViewModel(),
) {
	val context = LocalContext.current
	val timerUiState by viewModel.timerUiState.collectAsStateWithLifecycle()

	TimerContent(
		appName = appName,
		onSetTime = viewModel::setTime,
		onConfirm = viewModel::initTimeSetting,
		onCloseOverlay = onCloseOverlay,
		onExitManageApp = onExitManageApp,
		onTimerConfirm = {
			viewModel.setBreakTimeAlarm(groupId, groupName)
		},
		onBackPressToInit = viewModel::resetToInitialState,
		timerUiState = timerUiState,
	)

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
	onBackPressToInit: () -> Unit,
	timerUiState: TimerUiState,
) {
	when (timerUiState) {
		TimerUiState.Init -> {
			InitScreen(
				appName = appName,
				onConfirm = onConfirm,
				onExitManageApp = onExitManageApp,
			)
		}

		is TimerUiState.TimeSetting -> {
			BackHandler {
				onBackPressToInit()
			}

			TimerScreen(
				appName = appName,
				onTimeChange = onSetTime,
				onComplete = onTimerConfirm,
			)
		}

		is TimerUiState.SetComplete -> {
			BackHandler {
				onCloseOverlay()
			}

			SetCompleteScreen(
				durationMinutes = timerUiState.durationMinutes,
				endTime = timerUiState.endTime,
				onCloseOverlay = onCloseOverlay,
			)
		}
	}
}
