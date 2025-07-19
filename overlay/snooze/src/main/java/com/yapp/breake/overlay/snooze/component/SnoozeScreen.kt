package com.yapp.breake.overlay.snooze.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.overlay.ui.OverlayBase
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
internal fun SnoozeScreen(
	snoozeCount: Int,
	onFinishApp: () -> Unit,
	onSnooze: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_blocking,
		titleRes = UiRes.string.blocking_title,
		buttonText = stringResource(id = UiRes.string.btn_exit),
		onButtonClick = onFinishApp,
		textButtonText = stringResource(
			id = UiRes.string.blocking_check_time,
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
			onFinishApp = { /* Do nothing */ },
			onSnooze = { /* Do nothing */ },
		)
	}
}
