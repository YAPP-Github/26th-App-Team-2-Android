package com.yapp.breake.overlay.snooze.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.overlay.ui.OverlayBase
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
internal fun SnoozeBlocking(
	groupName: String,
	onExitManageApp: () -> Unit,
	onStartHome: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_cooldown,
		title = stringResource(
			UiRes.string.snooze_blocking_title,
			Constants.SNOOZE_MINUTES,
			groupName,
		),
		buttonText = stringResource(id = UiRes.string.btn_check_time),
		onButtonClick = onStartHome,
		textButtonText = stringResource(id = UiRes.string.btn_exit),
		onTextButtonClick = onExitManageApp,
		contentDescriptionRes = stringResource(
			id = UiRes.string.snooze_blocking_description,
		),
	)
}

@Preview
@Composable
private fun SnoozeBlockingPreview() {
	BrakeTheme {
		SnoozeBlocking(
			groupName = "SNS",
			onExitManageApp = { /* Do nothing */ },
			onStartHome = { /* Do nothing */ },
		)
	}
}
