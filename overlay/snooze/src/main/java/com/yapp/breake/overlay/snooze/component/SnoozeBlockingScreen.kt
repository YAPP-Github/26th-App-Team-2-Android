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
	onExitManageApp: () -> Unit,
	onStartHome: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_cooldown,
		title = stringResource(UiRes.string.cooldown_title),
		buttonText = stringResource(id = UiRes.string.btn_check_time),
		onButtonClick = onStartHome,
		textButtonText = stringResource(id = UiRes.string.btn_exit),
		onTextButtonClick = onExitManageApp,
		contentDescriptionRes = stringResource(
			id = UiRes.string.cooldown_description,
			Constants.SNOOZE_MINUTES,
		),
	)
}

@Preview
@Composable
private fun SnoozeBlockingPreview() {
	BrakeTheme {
		SnoozeBlocking(
			onExitManageApp = { /* Do nothing */ },
			onStartHome = { /* Do nothing */ },
		)
	}
}
