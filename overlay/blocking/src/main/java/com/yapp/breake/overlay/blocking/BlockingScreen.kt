package com.yapp.breake.overlay.blocking

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.overlay.ui.OverlayBase
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
fun BlockingOverlay(
	onStartHome: () -> Unit,
	onFinishApp: () -> Unit,
) {
	BlockingScreen(
		onStartHome = onStartHome,
		onFinishApp = onFinishApp,
	)
}

@Composable
private fun BlockingScreen(
	onStartHome: () -> Unit,
	onFinishApp: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_cooldown,
		titleRes = UiRes.string.blocking_title,
		buttonText = stringResource(id = UiRes.string.btn_check_time),
		onButtonClick = onStartHome,
		textButtonText = stringResource(id = UiRes.string.btn_exit),
		onTextButtonClick = onFinishApp,
		contentDescriptionRes = stringResource(
			id = UiRes.string.blocking_description,
			Constants.SNOOZE_MINUTES,
		),
	)
}

@Preview
@Composable
private fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingScreen(
			onStartHome = {},
			onFinishApp = {},
		)
	}
}
