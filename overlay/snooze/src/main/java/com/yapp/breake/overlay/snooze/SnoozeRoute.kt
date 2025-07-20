package com.yapp.breake.overlay.snooze

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.navigation.compositionlocal.LocalMainAction
import com.yapp.breake.overlay.snooze.component.SnoozeBlocking
import com.yapp.breake.overlay.snooze.component.SnoozeScreen

@Composable
fun SnoozeRoute(
	groupId: Long,
	snoozesCount: Int,
	onFinishApp: () -> Unit,
	onStartHome: () -> Unit,
) {
	SnoozeOverlay(
		groupId = groupId,
		snoozesCount = snoozesCount,
		onFinishApp = onFinishApp,
		onStartHome = onStartHome,
	)
}

@Composable
private fun SnoozeOverlay(
	groupId: Long,
	snoozesCount: Int,
	onFinishApp: () -> Unit,
	onStartHome: () -> Unit,
	viewModel: SnoozeViewModel = hiltViewModel(),
) {
	val mainAction = LocalMainAction.current

	if (snoozesCount < Constants.MAX_SNOOZE_COUNT) {
		SnoozeScreen(
			snoozeCount = snoozesCount,
			onFinishApp = onFinishApp,
			onSnooze = {
				viewModel.setSnooze(groupId)
				onFinishApp()
			},
		)
	} else {
		SnoozeBlocking(
			onFinishApp = onFinishApp,
			onStartHome = onStartHome,
		)
	}

	LaunchedEffect(Unit) {
		viewModel.toastEffect.collect { message ->
			mainAction.onShowToast(message)
		}
	}
}
