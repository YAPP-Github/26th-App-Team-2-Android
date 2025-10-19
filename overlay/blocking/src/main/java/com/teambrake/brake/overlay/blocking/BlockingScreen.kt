package com.teambrake.brake.overlay.blocking

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.teambrake.brake.core.common.Constants
import com.teambrake.brake.core.designsystem.theme.BrakeTheme
import com.teambrake.brake.core.util.addJosaEulReul
import com.teambrake.brake.overlay.ui.OverlayBase
import com.teambrake.brake.overlay.ui.R as UiRes

@Composable
fun BlockingOverlay(
	appName: String,
	groupName: String,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	BlockingScreen(
		appName = appName,
		groupName = groupName,
		onStartHome = onStartHome,
		onExitManageApp = onExitManageApp,
	)
}

@Composable
private fun BlockingScreen(
	appName: String,
	groupName: String,
	onStartHome: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_cooldown,
		title = stringResource(
			id = UiRes.string.blocking_title,
			appName.addJosaEulReul(),
		),
		buttonText = stringResource(id = UiRes.string.btn_check_time),
		onButtonClick = onStartHome,
		textButtonText = stringResource(id = UiRes.string.btn_exit),
		onTextButtonClick = onExitManageApp,
		contentDescriptionRes = stringResource(
			UiRes.string.blocking_description,
			Constants.SNOOZE_MINUTES,
			groupName,
		),
	)
}

@Preview
@Composable
private fun BlockingScreenPreview() {
	BrakeTheme {
		BlockingScreen(
			appName = "Instagram",
			groupName = "SNS",
			onStartHome = {},
			onExitManageApp = {},
		)
	}
}
