package com.yapp.breake.overlay.snooze

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.overlay.snooze.component.SnoozeBlocking
import com.yapp.breake.overlay.snooze.component.SnoozeScreen

@Composable
fun SnoozeRoute(
	groupId: Long,
	snoozeCount: Int,
) {
}

@Composable
private fun SnoozeOverlay(
	groupId: Long,
	snoozeCount: Int,
	viewModel: SnoozeViewModel = hiltViewModel(),
) {

}

@Composable
private fun SnoozeContent(
	snoozeCount: Int,
	snoozeEnabled: Boolean,
) {
	if (snoozeEnabled) {
		SnoozeScreen(
			snoozeCount = snoozeCount,
			onExit = { /* Handle exit action */ },
			onSnooze = { /* Handle snooze action */ },
		)
	} else {
		SnoozeBlocking(
			onExit = { /* Handle exit action */ },
			onSnooze = { /* Handle snooze action */ },
		)
	}
}
