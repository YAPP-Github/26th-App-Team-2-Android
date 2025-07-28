package com.yapp.breake.presentation.setting.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.yapp.breake.core.designsystem.component.BaseDialog
import com.yapp.breake.core.designsystem.component.DialogButton
import com.yapp.breake.core.designsystem.theme.BrakeTheme
import com.yapp.breake.core.designsystem.theme.Gray800
import com.yapp.breake.core.designsystem.theme.White
import com.yapp.breake.presentation.setting.R

@Composable
internal fun LogoutWarningDialog(
	onDismissRequest: () -> Unit,
	onConfirm: () -> Unit,
) {
	BaseDialog(
		onDismissRequest = onDismissRequest,
		dismissButton = {
			DialogButton(
				text = stringResource(R.string.setting_logout_dialog_dismiss_text),
				onClick = onDismissRequest,
				containerColor = Gray800,
				contentColor = White,
			)
		},
		confirmButton = {
			DialogButton(
				text = stringResource(R.string.setting_logout_dialog_confirm_text),
				onClick = onConfirm,
			)
		},
	) {
		Text(
			modifier = Modifier.fillMaxWidth(),
			text = stringResource(R.string.setting_logout_dialog_title),
			style = BrakeTheme.typography.subtitle22SB,
			color = White,
			textAlign = TextAlign.Center,
		)
	}
}

@Preview
@Composable
fun LogoutWarningDialogPreview() {
	BrakeTheme {
		LogoutWarningDialog(
			onDismissRequest = {},
			onConfirm = {},
		)
	}
}
