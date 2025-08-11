package com.yapp.breake.overlay.snooze

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.yapp.breake.core.common.Constants
import com.yapp.breake.overlay.snooze.component.SnoozeBlocking
import com.yapp.breake.overlay.snooze.component.SnoozeScreen

@Composable
fun SnoozeRoute(
	groupId: Long,
	appName: String,
	groupName: String,
	snoozesCount: Int,
	onCloseOverlay: () -> Unit,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	SnoozeOverlay(
		groupId = groupId,
		appName = appName,
		groupName = groupName,
		snoozesCount = snoozesCount,
		onCloseOverlay = onCloseOverlay,
		onStartHome = onStartHome,
		onExitManageApp = onExitManageApp,
	)
}

@Composable
private fun SnoozeOverlay(
	groupId: Long,
	appName: String,
	groupName: String,
	snoozesCount: Int,
	onCloseOverlay: () -> Unit,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
	viewModel: SnoozeViewModel = hiltViewModel(),
) {
	val context = LocalContext.current
	val lifecycleOwner = LocalLifecycleOwner.current

	DisposableEffect(lifecycleOwner) {
		val observer = LifecycleEventObserver { _, event ->
			if (event == Lifecycle.Event.ON_STOP) {
				viewModel.setBlock(groupId, appName)
			}
		}
		lifecycleOwner.lifecycle.addObserver(observer)
		onDispose {
			lifecycleOwner.lifecycle.removeObserver(observer)
		}
	}

	if (snoozesCount < Constants.MAX_SNOOZE_COUNT) {
		SnoozeScreen(
			snoozeCount = snoozesCount,
			onExitManageApp = {
				viewModel.setBlock(groupId, appName)
				onExitManageApp()
			},
			onSnooze = {
				viewModel.setSnooze(groupId, appName)
				onCloseOverlay()
			},
		)
	} else {
		SnoozeBlocking(
			groupName = groupName,
			onExitManageApp = onExitManageApp,
			onStartHome = onStartHome,
		)
	}

	LaunchedEffect(Unit) {
		viewModel.toastEffect.collect { message ->
			Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
		}
	}
}
