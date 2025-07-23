package com.yapp.breake.overlay.blocking

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.common.Constants
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.util.addJosaEulReul
import com.yapp.breake.overlay.ui.OverlayBase
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
fun BlockingOverlay(
	appName: String,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	BlockingScreen(
		appName = appName,
		onStartHome = onStartHome,
		onExitManageApp = onExitManageApp,
	)
}

@Composable
private fun BlockingScreen(
	appName: String,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_cooldown,
		title = stringResource(
			id = UiRes.string.blocking_title,
			Constants.SNOOZE_MINUTES,
			appName.addJosaEulReul(),
		),
		buttonText = stringResource(id = UiRes.string.btn_check_time),
		onButtonClick = onStartHome,
		textButtonText = stringResource(id = UiRes.string.btn_exit),
		onTextButtonClick = onExitManageApp,
		contentDescriptionRes = stringResource(UiRes.string.blocking_content),
	)
}

@Preview
@Composable
private fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingScreen(
			"유튜브",
			onStartHome = {},
			onExitManageApp = {},
		)
	}
}
