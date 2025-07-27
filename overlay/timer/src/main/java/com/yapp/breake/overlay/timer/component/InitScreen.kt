package com.yapp.breake.overlay.timer.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.util.addJosaEulReul
import com.yapp.breake.overlay.ui.OverlayBase
import com.yapp.breake.overlay.ui.R as UiRes

@Composable
internal fun InitScreen(
	appName: String,
	onConfirm: () -> Unit,
	onExitManageApp: () -> Unit,
) {
	OverlayBase(
		imageRes = UiRes.drawable.img_init,
		title = stringResource(UiRes.string.init_title, appName.addJosaEulReul()),
		buttonText = stringResource(id = UiRes.string.btn_use),
		onButtonClick = onConfirm,
		textButtonText = stringResource(id = UiRes.string.btn_not_use),
		onTextButtonClick = onExitManageApp,
	)
}

@Preview
@Composable
private fun InitScreenPreview() {
	BrakeTheme {
		InitScreen(
			appName = "인스타그램",
			onConfirm = {},
			onExitManageApp = {},
		)
	}
}
