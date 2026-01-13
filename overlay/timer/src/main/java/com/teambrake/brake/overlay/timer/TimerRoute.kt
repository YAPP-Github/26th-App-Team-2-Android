package com.teambrake.brake.overlay.timer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.teambrake.brake.overlay.timer.component.InitScreen
import com.teambrake.brake.overlay.timer.component.SetCompleteScreen

@Composable
fun TimerRoute(
	appName: String,
	groupName: String,
	groupId: Long,
	groupAppCount: Int,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
) {
	TimerOverlay(
		appName = appName,
		groupName = groupName,
		groupId = groupId,
		groupAppCount = groupAppCount,
		onExitManageApp = onExitManageApp,
		onCloseOverlay = onCloseOverlay,
	)
}

@Composable
private fun TimerOverlay(
	appName: String,
	groupName: String,
	groupId: Long,
	groupAppCount: Int,
	onExitManageApp: () -> Unit,
	onCloseOverlay: () -> Unit,
) {
	// hiltViewModel의 내부 factory 인자를 통해 파라미터 전달
	val viewModel: TimerViewModel = hiltViewModel(
		creationCallback = { factory: TimerViewModel.TimerFactory ->
			factory.create(groupId, groupName, groupAppCount)
		},
	)
	val context = LocalContext.current
	val timerUiState by viewModel.timerUiState.collectAsStateWithLifecycle()

	TimerContent(
		appName = appName,
		onStart = viewModel::initTimeSetting,
		onChangeTime = viewModel::changeTime,
		onSetTime = viewModel::setTime,
		onConfirm = {
			viewModel.confirmBreakTimeAlarm()
			onCloseOverlay()
		},
		onExitManageApp = onExitManageApp,
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
	onStart: () -> Unit,
	onChangeTime: (Int) -> Unit,
	onSetTime: () -> Unit,
	onConfirm: () -> Unit,
	onExitManageApp: () -> Unit,
	onBackPressToInit: () -> Unit,
	timerUiState: TimerUiState,
) {
	when (timerUiState) {
		TimerUiState.Init -> {
			InitScreen(
				appName = appName,
				onStart = onStart,
				onExitManageApp = onExitManageApp,
			)
		}

		is TimerUiState.TimeSetting -> {
			BackHandler {
				onBackPressToInit()
			}

			TimerScreen(
				appName = appName,
				onTimeChange = onChangeTime,
				onSetTime = onSetTime,
			)
		}

		is TimerUiState.SetComplete -> {
			BackHandler {
				onConfirm()
			}

			SetCompleteScreen(
				durationMinutes = timerUiState.durationMinutes,
				endTime = timerUiState.endTime,
				onCloseOverlay = onConfirm,
			)
		}
	}
}
