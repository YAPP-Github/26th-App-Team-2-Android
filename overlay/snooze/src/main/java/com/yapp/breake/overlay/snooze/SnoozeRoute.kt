package com.yapp.breake.overlay.snooze

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.yapp.breake.core.common.Constants
import com.yapp.breake.overlay.snooze.component.SnoozeBlocking
import com.yapp.breake.overlay.snooze.component.SnoozeScreen

@Composable
fun SnoozeRoute(
	groupId: Long,
	groupName: String,
	snoozesCount: Int,
	onCloseOverlay: () -> Unit,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	SnoozeOverlay(
		groupId = groupId,
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
	groupName: String,
	snoozesCount: Int,
	onCloseOverlay: () -> Unit,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
	viewModel: SnoozeViewModel = hiltViewModel(),
) {
	val context = LocalContext.current

	if (snoozesCount < Constants.MAX_SNOOZE_COUNT) {
		SnoozeScreen(
			snoozeCount = snoozesCount,
			onExitManageApp = onExitManageApp,
			onSnooze = {
				viewModel.setSnooze(groupId, groupName)
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
