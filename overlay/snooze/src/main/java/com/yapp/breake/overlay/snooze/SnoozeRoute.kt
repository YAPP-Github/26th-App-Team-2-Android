package com.yapp.breake.overlay.snooze

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.common.Constants
import com.yapp.breake.overlay.snooze.component.SnoozeBlocking
import com.yapp.breake.overlay.snooze.component.SnoozeScreen

@Composable
fun SnoozeRoute(
	groupId: Long,
	snoozeCount: Int,
	onFinishApp: () -> Unit,
	onStartHome: () -> Unit,
) {
	SnoozeOverlay(
		groupId = groupId,
		snoozeCount = snoozeCount,
		onFinishApp = onFinishApp,
		onStartHome = onStartHome,
	)
}

@Composable
private fun SnoozeOverlay(
	groupId: Long,
	snoozeCount: Int,
	onFinishApp: () -> Unit,
	onStartHome: () -> Unit,
	viewModel: SnoozeViewModel = hiltViewModel(),
) {
	if (snoozeCount < Constants.MAX_SNOOZE_COUNT) {
		SnoozeScreen(
			snoozeCount = snoozeCount,
			onFinishApp = onFinishApp,
			onSnooze = {
				viewModel.setSnooze(groupId)
			},
		)
	} else {
		SnoozeBlocking(
			onFinishApp = onFinishApp,
			onStartHome = onStartHome,
		)
	}
}
