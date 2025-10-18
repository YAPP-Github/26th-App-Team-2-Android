package com.teambrake.brake.overlay.snooze.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teambrake.brake.core.common.Constants
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.overlay.ui.OverlayBase
import com.teambrake.brake.overlay.ui.R as UiRes

@Composable
internal fun SnoozeScreen(
	snoozeCount: Int,
	onExitManageApp: () -> Unit,
	onSnooze: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_blocking,
		title = stringResource(UiRes.string.snooze_title),
		buttonText = stringResource(id = UiRes.string.btn_exit),
		onButtonClick = onExitManageApp,
		textButtonText = stringResource(
			id = UiRes.string.btn_more_time,
			snoozeCount,
			Constants.MAX_SNOOZE_COUNT,
		),
		onTextButtonClick = onSnooze,
	)
}

@Preview
@Composable
private fun SnoozeScreenPreview() {
	BrakeTheme {
		SnoozeScreen(
			snoozeCount = 2,
			onExitManageApp = { /* Do nothing */ },
			onSnooze = { /* Do nothing */ },
		)
	}
}
